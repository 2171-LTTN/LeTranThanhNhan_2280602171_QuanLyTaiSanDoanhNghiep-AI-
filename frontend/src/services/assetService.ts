import api from './api';
import type { Asset, CreateAssetRequest, UpdateAssetRequest, AssignAssetRequest } from '../types';

export const assetService = {
  getAll: async (): Promise<Asset[]> => {
    const response = await api.get<{ data: Asset[] }>('/assets');
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
};
