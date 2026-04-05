import api from './api';
import type { AssetRequest, CreateAllocationRequest, ReviewAssetRequest } from '../types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
}

export const requestService = {
  create: async (data: CreateAllocationRequest): Promise<AssetRequest> => {
    const response = await api.post<{ data: AssetRequest }>('/requests/me/allocation-requests', data);
    return response.data.data;
  },

  getMy: async (): Promise<AssetRequest[]> => {
    const response = await api.get<{ data: AssetRequest[] }>('/requests/my');
    return response.data.data;
  },

  getAll: async (page: number = 0, size: number = 20): Promise<PageResponse<AssetRequest>> => {
    const response = await api.get<{ data: PageResponse<AssetRequest> }>('/requests', {
      params: { page, size },
    });
    return response.data.data;
  },

  getById: async (id: string): Promise<AssetRequest> => {
    const response = await api.get<{ data: AssetRequest }>(`/requests/${id}`);
    return response.data.data;
  },

  cancel: async (id: string): Promise<AssetRequest> => {
    const response = await api.post<{ data: AssetRequest }>(`/requests/${id}/cancel`);
    return response.data.data;
  },

  approve: async (id: string, data: ReviewAssetRequest): Promise<AssetRequest> => {
    const response = await api.post<{ data: AssetRequest }>(`/requests/${id}/approve`, data);
    return response.data.data;
  },

  reject: async (id: string, data: ReviewAssetRequest): Promise<AssetRequest> => {
    const response = await api.post<{ data: AssetRequest }>(`/requests/${id}/reject`, data);
    return response.data.data;
  },

  getStats: async (): Promise<{ pendingRequests: number }> => {
    const response = await api.get<{ data: { pendingRequests: number } }>('/requests/stats');
    return response.data.data;
  },

  getMyStats: async (): Promise<{ myPendingRequests: number }> => {
    const response = await api.get<{ data: { myPendingRequests: number } }>('/requests/my-stats');
    return response.data.data;
  },
};
