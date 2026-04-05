import api from './api';
import type { AssetHistory } from '../types';

function asHistoryArray(payload: unknown): AssetHistory[] {
  return Array.isArray(payload) ? payload : [];
}

export const historyService = {
  getAll: async (): Promise<AssetHistory[]> => {
    const response = await api.get<{ data?: AssetHistory[] }>('/assets/histories');
    return asHistoryArray(response.data?.data);
  },

  getByAssetId: async (assetId: string): Promise<AssetHistory[]> => {
    const response = await api.get<{ data?: AssetHistory[] }>(`/assets/${assetId}/history`);
    return asHistoryArray(response.data?.data);
  },
};
