import api from './api';
import type { AssetHistory } from '../types';

export const historyService = {
  getAll: async (): Promise<AssetHistory[]> => {
    const response = await api.get<{ data: AssetHistory[] }>('/asset-histories');
    return response.data.data;
  },

  getByAssetId: async (assetId: string): Promise<AssetHistory[]> => {
    const response = await api.get<{ data: AssetHistory[] }>(`/asset-histories/asset/${assetId}`);
    return response.data.data;
  },
};
