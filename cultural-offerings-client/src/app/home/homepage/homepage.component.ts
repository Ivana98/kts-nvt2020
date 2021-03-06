import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MapInfoWindow, MapMarker, GoogleMap, MapAnchorPoint } from '@angular/google-maps';
import { Router } from '@angular/router';
import { MdbCheckboxChange } from 'angular-bootstrap-md';
import { AbstractCrudService } from 'src/app/core/model/abstract-crud-service';
import { CulturalOffering } from 'src/app/core/model/cultural-offering';
import { CulturalOfferingSubtype } from 'src/app/core/model/cultural-offering-subtype';
import { CulturalOfferingType } from 'src/app/core/model/cultural-offering-type';
import { SearchFilter } from 'src/app/core/model/search-filter';
import { CulturalOfferingSubtypeService } from 'src/app/core/services/cultural-offering-subtype/cultural-offering-subtype.service';
import { CulturalOfferingTypeService } from 'src/app/core/services/cultural-offering-type/cultural-offering-type.service';
import { CulturalOfferingService } from 'src/app/core/services/cultural-offering/cultural-offering.service';
import { AuthService } from 'src/app/core/services/security/auth-service/auth.service';

declare var ol: any;

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
})

export class HomepageComponent implements OnInit {
  @ViewChild(GoogleMap) map: GoogleMap;
  @ViewChild(MapInfoWindow) infoWindow: MapInfoWindow;
  @ViewChild('searchFilter') searchFilter;

  // Mapa
  center: google.maps.LatLngLiteral = {lat: 44, lng: 20.7};
  markerPositions: google.maps.LatLngLiteral[] = [];
  zoom = 7;
  options: google.maps.MapOptions = {
    mapTypeControl: false,
  };
  infoWindowOptions: google.maps.InfoWindowOptions;

  // Podaci
  culturalOfferings: CulturalOffering[] = [];
  culturalOfferingTypes: CulturalOfferingType[] = [];
  culturalOfferingSubtypes: Map<number, CulturalOfferingSubtype[]> = new Map<number, CulturalOfferingSubtype[]>();

  searchFilterForm: FormGroup;

  constructor(
    public culturalOfferingService: CulturalOfferingService,
    public culturalOfferingTypeService: CulturalOfferingTypeService,
    public culturalOfferingSubtypeService: CulturalOfferingSubtypeService,
    public elRef: ElementRef,
    public formBuilder: FormBuilder,
    public router: Router,
    public authService: AuthService
  ) {
    this.searchFilterForm = formBuilder.group({
      termField: [''],
      subscriptionsField: ['']
    });
  }

  ngOnInit(): void {
    this.fetchAllCulturalOffering();
    this.fetchAllCulturalOfferingTypes();
  }

  addSearchFilter(): void {
    const div = this.elRef.nativeElement.querySelector('#searchFilter');
    if (this.map.controls[google.maps.ControlPosition.LEFT_TOP].getLength() === 0) {
      this.map.controls[google.maps.ControlPosition.LEFT_TOP].push(div);
    }
  }

  addMarker(event: google.maps.MapMouseEvent): void {
    this.markerPositions.push(event.latLng.toJSON());
  }

  openInfoWindow(marker: MapMarker): void {
    this.infoWindow.open(marker);
  }

  async openPeekInfoWindow(marker: MapMarker, culturalOffering: CulturalOffering): Promise<void> {
    this.infoWindow.options = {content: this.formatInfoWindowContent(culturalOffering)};
    this.infoWindow.open(marker);
  }

  closePeekInfoWindow(): void {
    this.infoWindow.close();
  }
  formatInfoWindowContent(culturalOffering: CulturalOffering): string {
    let retval = '';
    retval += `<h4 style="opacity: 0.7">${culturalOffering.culturalOfferingTypeName} <b>/</b> ${culturalOffering.culturalOfferingSubtypeName}</h4>`;
    retval += `<h2>${culturalOffering.name}</h2>`;
    retval += `<h4>${culturalOffering.description}</h4>`;
    return retval;
  }

  async fetchAllCulturalOffering(): Promise<void> {
    this.culturalOfferingService.getAll({
      page: 0,
      size: 1000,
      sort: '',
      sortOrder: ''
    })
    .subscribe((page) => {
      this.culturalOfferings = [...page.content];
    });
  }

  async fetchAllCulturalOfferingTypes(): Promise<void> {
    this.culturalOfferingTypeService.getAll({
      page: 0,
      size: 100,
      sort: '',
      sortOrder: ''
    })
    .subscribe((page) => {
      this.fetchAllCulturalOfferingSubtypes(page.content);
      this.culturalOfferingTypes = [...page.content];
      for (const culturalOfferingType of this.culturalOfferingTypes) {
        const formControlName = `type${culturalOfferingType.id}`;
        this.searchFilterForm.addControl(formControlName, new FormControl());
        const temp = {};
        temp[formControlName] = true;
        this.searchFilterForm.patchValue(temp);
      }
    });
  }

  fetchAllCulturalOfferingSubtypes(types: CulturalOfferingType[]): void {
    for (const culturalOfferingType of types) {
      this.culturalOfferingSubtypeService.getAllByTypeId(culturalOfferingType.id)
      .subscribe((culturalOfferingSubtypes: CulturalOfferingSubtype[]) => {
        this.culturalOfferingSubtypes.set(culturalOfferingType.id, culturalOfferingSubtypes);
        for (const culturalOfferingSubtype of this.culturalOfferingSubtypes.get(culturalOfferingType.id)) {
          const formControlName = `subtype${culturalOfferingSubtype.id}`;
          this.searchFilterForm.addControl(formControlName, new FormControl());
          const temp = {};
          temp[formControlName] = true;
          this.searchFilterForm.patchValue(temp);
        }
      });
    }
  }

  formatPosition(culturalOffering: CulturalOffering): google.maps.LatLng {
    return new google.maps.LatLng(
      culturalOffering.latitude,
      culturalOffering.longitude
    );
  }

  typeChecked(event: MdbCheckboxChange, culturalOfferingType: CulturalOfferingType): void {
    const newValue = {};
    for (const culturalOfferingSubtype of this.culturalOfferingSubtypes.get(culturalOfferingType.id)) {
      if (event.checked) {
        newValue[`subtype${culturalOfferingSubtype.id}`] = true;
      }
      else {
        newValue[`subtype${culturalOfferingSubtype.id}`] = false;
      }
    }
    this.searchFilterForm.patchValue(newValue);
  }

  subtypeChecked(event: MdbCheckboxChange, culturalOfferingSubtype: CulturalOfferingSubtype): void {
    const newValue = {};
    const typeId = culturalOfferingSubtype.typeId;
    if (event.checked) {
      for (const cos of this.culturalOfferingSubtypes.get(typeId)) {
        if (this.searchFilterForm.value[`subtype${cos.id}`] === false) {
          return;
        }
      }
      newValue[`type${typeId}`] = true;
    }
    else {
      newValue[`type${typeId}`] = false;
    }
    this.searchFilterForm.patchValue(newValue);
  }

  searchFilterApply(): void {
    const termField = 'termField';
    const subscriptionsField = 'subscriptionsField';
    const searchFilter: SearchFilter = {
      term: this.searchFilterForm.value[termField],
      culturalOfferingSubtypeIds: [],
      culturalOfferingTypeIds: [],
      subscriptions: this.searchFilterForm.value[subscriptionsField]
    };
    searchFilter.culturalOfferingTypeIds = [];
    searchFilter.culturalOfferingSubtypeIds = [];
    for (const val in this.searchFilterForm.value) {
      if (val.startsWith('type')) {
        if (this.searchFilterForm.value[val] === true) {
          const typeId = parseInt(val.substring(4));
          searchFilter.culturalOfferingTypeIds.push(typeId);
        }
      }
      else if (val.startsWith('subtype')) {
        if (this.searchFilterForm.value[val] === true) {
          const subtypeId = parseInt(val.substring(7));
          searchFilter.culturalOfferingSubtypeIds.push(subtypeId);
          const type = this.culturalOfferingTypes.filter(t => t.subTypeIds.includes(subtypeId));
          searchFilter.culturalOfferingTypeIds.push(type[0].id);
        }
      }
    }
    if (this.authService.isLoggedIn()) {
      this.culturalOfferingService.searchFilter(
        searchFilter, {
        page: 0,
        size: 1000,
        sort: '',
        sortOrder: ''
      })
      .subscribe((page) => {
        this.culturalOfferings = [...page.content];
      });
    }
    else {
      this.culturalOfferingService.searchFilterGuest(
        searchFilter, {
        page: 0,
        size: 1000,
        sort: '',
        sortOrder: ''
      })
      .subscribe((page) => {
        this.culturalOfferings = [...page.content];
      });
    }
  }

  openCulturalOffering(culturalOffering: CulturalOffering): void {
    if (this.authService.getUserRole() === 'ADMIN') {
      this.router.navigateByUrl(`admin/cultural-offering/${culturalOffering.id}`);
    }
    else {
      this.router.navigateByUrl(`cultural-offering/${culturalOffering.id}`);
    }
  }

  loggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}
