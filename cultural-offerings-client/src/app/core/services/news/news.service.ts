import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AbstractCrudService } from '../../model/abstract-crud-service';
import { News } from '../../model/news';
import { PageableRequest } from '../../model/pageable-request';
import { ApiService, RequestMethod } from '../security/api-service/api.service';

@Injectable()
export class NewsService implements AbstractCrudService<News>{

  endpoint: string = `${environment.baseUrl}/news`

  constructor(public apiService: ApiService) { }

  getAll(pageRequest: PageableRequest): Observable<any> {
    var id = this.getSelectedOfferingId();
    return this.apiService.request(
      `${this.endpoint}/all/by-page?id=${id}&page=${pageRequest.page}&size=${pageRequest.size}&sort=${pageRequest.sort},${pageRequest.sortOrder}`,
      {},
      RequestMethod.Post
    );
  }

  getOne(id: string) {
    return this.apiService.get(`${this.endpoint}/${id}`);
  }

  insert(entity: News): Observable<News> {
    return this.apiService.post(`${this.endpoint}`, entity);
  }

  update(entity: News): Observable<News> {
    return this.apiService.put(`${this.endpoint}/${entity.id}`, entity);
  }

  delete(id: number): Observable<void> {
    return this.apiService.delete(`${this.endpoint}/${id}`);
  }

  notify(id: number): Observable<Boolean> {
    console.log(id)
    return this.apiService.post(`${this.endpoint}/notify/${id}`);
  }

  getSelectedOfferingId() : number {
    return JSON.parse(localStorage.getItem('SELECTED_OFFERING'));
  }

  setSelectedOfferingId(id: number) : void {
    localStorage.setItem('SELECTED_OFFERING', JSON.stringify(id));
  }
}
