import api from './api';
import type { Asset, CreateAssetRequest, UpdateAssetRequest, AssignAssetRequest, AssetHistory } from '../types';

interface PageAssetResponse {
  content: Asset[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const assetService = {
  getAll: async (page: number = 0, size: number = 20): Promise<Asset[]> => {
    const response = await api.get<{ data: PageAssetResponse }>('/assets', {
      params: { page, size },
    });
    return response.data.data.content;
  },

  getMyAssets: async (page: number = 0, size: number = 100): Promise<Asset[]> => {
    const response = await api.get<{ data: PageAssetResponse }>('/assets/my-assets', {
      params: { page, size },
    });
    return response.data.data.content;
  },

  getMyHistories: async (): Promise<AssetHistory[]> => {
    const response = await api.get<{ data: AssetHistory[] }>('/assets/my-histories');
    return response.data.data;
  },

  getById: async (id: string): Promise<Asset> => {
    const response = await api.get<{ data: Asset }>(`/assets/${id}`);
    return response.data.data;
  },

  create: async (data: CreateAssetRequest): Promise<Asset> => {
    const response = await api.post<{ data: Asset }>('/assets', data);
    return response.data.data;
  },

  update: async (id: string, data: UpdateAssetRequest): Promise<Asset> => {
    const response = await api.put<{ data: Asset }>(`/assets/${id}`, data);
    return response.data.data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/assets/${id}`);
  },

  assign: async (assetId: string, data: AssignAssetRequest): Promise<Asset> => {
    const response = await api.post<{ data: Asset }>(`/assets/${assetId}/assign`, data);
    return response.data.data;
  },

  return: async (assetId: string): Promise<Asset> => {
    const response = await api.post<{ data: Asset }>(`/assets/${assetId}/return`);
    return response.data.data;
  },

  returnMine: async (assetId: string): Promise<Asset> => {
    const response = await api.post<{ data: Asset }>(`/assets/${assetId}/return-mine`);
    return response.data.data;
  },
};
