import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from '../services/api';
import assetService from '../services/assetService';

vi.mock('../services/api');

const mockedAxios = vi.mocked(axios);

describe('AssetService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getAllAssets', () => {
    it('should fetch all assets with pagination', async () => {
      const mockResponse = {
        content: [
          { id: '1', name: 'MacBook Pro', status: 'AVAILABLE' },
          { id: '2', name: 'Dell XPS', status: 'IN_USE' },
        ],
        totalElements: 2,
        totalPages: 1,
      };

      mockedAxios.get.mockResolvedValue({ data: mockResponse });

      const result = await assetService.getAllAssets(0, 10);

      expect(mockedAxios.get).toHaveBeenCalledWith('/assets', {
        params: { page: 0, size: 10 },
      });
      expect(result).toEqual(mockResponse);
    });

    it('should handle API errors gracefully', async () => {
      mockedAxios.get.mockRejectedValue(new Error('Network error'));

      await expect(assetService.getAllAssets(0, 10)).rejects.toThrow('Network error');
    });
  });

  describe('getAssetById', () => {
    it('should fetch asset by ID', async () => {
      const mockAsset = { id: 'asset-1', name: 'MacBook Pro', status: 'AVAILABLE' };
      mockedAxios.get.mockResolvedValue({ data: mockAsset });

      const result = await assetService.getAssetById('asset-1');

      expect(mockedAxios.get).toHaveBeenCalledWith('/assets/asset-1');
      expect(result).toEqual(mockAsset);
    });
  });

  describe('createAsset', () => {
    it('should create a new asset', async () => {
      const newAsset = {
        name: 'New Laptop',
        category: 'Laptop',
        serialNumber: 'SN123',
        purchasePrice: 30000000,
        purchaseDate: '2024-01-15',
        warrantyUntil: '2027-01-15',
      };

      const mockResponse = { id: 'new-1', ...newAsset, status: 'AVAILABLE' };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await assetService.createAsset(newAsset);

      expect(mockedAxios.post).toHaveBeenCalledWith('/assets', newAsset);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('updateAsset', () => {
    it('should update an existing asset', async () => {
      const updateData = { name: 'Updated Name' };
      const mockResponse = { id: 'asset-1', name: 'Updated Name' };
      mockedAxios.put.mockResolvedValue({ data: mockResponse });

      const result = await assetService.updateAsset('asset-1', updateData);

      expect(mockedAxios.put).toHaveBeenCalledWith('/assets/asset-1', updateData);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('deleteAsset', () => {
    it('should delete an asset', async () => {
      mockedAxios.delete.mockResolvedValue({ data: { message: 'Deleted' } });

      await assetService.deleteAsset('asset-1');

      expect(mockedAxios.delete).toHaveBeenCalledWith('/assets/asset-1');
    });
  });

  describe('assignAsset', () => {
    it('should assign asset to user', async () => {
      const assignData = { userId: 'user-123' };
      const mockResponse = { id: 'asset-1', assignedTo: 'user-123', status: 'IN_USE' };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await assetService.assignAsset('asset-1', assignData);

      expect(mockedAxios.post).toHaveBeenCalledWith('/assets/asset-1/assign', assignData);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('returnAsset', () => {
    it('should return assigned asset', async () => {
      const mockResponse = { id: 'asset-1', status: 'AVAILABLE', assignedTo: null };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await assetService.returnAsset('asset-1');

      expect(mockedAxios.post).toHaveBeenCalledWith('/assets/asset-1/return');
      expect(result).toEqual(mockResponse);
    });
  });

  describe('reportBroken', () => {
    it('should report asset as broken', async () => {
      const mockResponse = { id: 'asset-1', status: 'BROKEN' };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await assetService.reportBroken('asset-1');

      expect(mockedAxios.post).toHaveBeenCalledWith('/assets/asset-1/report-broken');
      expect(result).toEqual(mockResponse);
    });
  });
});
