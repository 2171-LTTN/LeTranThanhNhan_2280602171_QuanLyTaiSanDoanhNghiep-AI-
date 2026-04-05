import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from '../services/api';
import requestService from '../services/requestService';

vi.mock('../services/api');

const mockedAxios = vi.mocked(axios);

describe('RequestService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getMyRequests', () => {
    it('should fetch user own requests', async () => {
      const mockRequests = [
        { id: 'req-1', status: 'PENDING', category: 'LAPTOP' },
        { id: 'req-2', status: 'APPROVED', category: 'MONITOR' },
      ];
      mockedAxios.get.mockResolvedValue({ data: mockRequests });

      const result = await requestService.getMyRequests();

      expect(mockedAxios.get).toHaveBeenCalledWith('/asset-requests/my');
      expect(result).toEqual(mockRequests);
    });
  });

  describe('getAllRequests', () => {
    it('should fetch all requests for admin', async () => {
      const mockResponse = {
        content: [{ id: 'req-1', status: 'PENDING' }],
        totalElements: 1,
      };
      mockedAxios.get.mockResolvedValue({ data: mockResponse });

      const result = await requestService.getAllRequests(0, 10);

      expect(mockedAxios.get).toHaveBeenCalledWith('/asset-requests', {
        params: { page: 0, size: 10 },
      });
      expect(result).toEqual(mockResponse);
    });
  });

  describe('getPendingRequests', () => {
    it('should fetch pending requests', async () => {
      const mockResponse = {
        content: [{ id: 'req-1', status: 'PENDING' }],
        totalElements: 1,
      };
      mockedAxios.get.mockResolvedValue({ data: mockResponse });

      const result = await requestService.getPendingRequests(0, 10);

      expect(mockedAxios.get).toHaveBeenCalledWith('/asset-requests/pending', {
        params: { page: 0, size: 10 },
      });
    });
  });

  describe('createRequest', () => {
    it('should create new asset request', async () => {
      const requestData = {
        category: 'LAPTOP',
        reason: 'Need for work',
        note: 'Remote work setup',
      };
      const mockResponse = { id: 'new-req', ...requestData, status: 'PENDING' };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await requestService.createRequest(requestData);

      expect(mockedAxios.post).toHaveBeenCalledWith('/asset-requests', requestData);
      expect(result).toEqual(mockResponse);
    });
  });

  describe('approveRequest', () => {
    it('should approve request and assign asset', async () => {
      const reviewData = {
        assetId: 'asset-123',
        reviewNote: 'Approved for remote work',
      };
      const mockResponse = { id: 'req-1', status: 'APPROVED', ...reviewData };
      mockedAxios.put.mockResolvedValue({ data: mockResponse });

      const result = await requestService.approveRequest('req-1', reviewData);

      expect(mockedAxios.put).toHaveBeenCalledWith('/asset-requests/req-1/approve', reviewData);
      expect(result.status).toBe('APPROVED');
    });
  });

  describe('rejectRequest', () => {
    it('should reject request with note', async () => {
      const reviewData = { reviewNote: 'Budget constraints' };
      const mockResponse = { id: 'req-1', status: 'REJECTED', ...reviewData };
      mockedAxios.put.mockResolvedValue({ data: mockResponse });

      const result = await requestService.rejectRequest('req-1', reviewData);

      expect(mockedAxios.put).toHaveBeenCalledWith('/asset-requests/req-1/reject', reviewData);
      expect(result.status).toBe('REJECTED');
    });
  });

  describe('cancelRequest', () => {
    it('should cancel own request', async () => {
      const mockResponse = { id: 'req-1', status: 'CANCELLED' };
      mockedAxios.put.mockResolvedValue({ data: mockResponse });

      const result = await requestService.cancelRequest('req-1');

      expect(mockedAxios.put).toHaveBeenCalledWith('/asset-requests/req-1/cancel');
      expect(result.status).toBe('CANCELLED');
    });
  });

  describe('getRequestById', () => {
    it('should fetch request by ID', async () => {
      const mockRequest = { id: 'req-1', status: 'PENDING' };
      mockedAxios.get.mockResolvedValue({ data: mockRequest });

      const result = await requestService.getRequestById('req-1');

      expect(mockedAxios.get).toHaveBeenCalledWith('/asset-requests/req-1');
      expect(result).toEqual(mockRequest);
    });
  });

  describe('countPendingRequests', () => {
    it('should return pending request count', async () => {
      mockedAxios.get.mockResolvedValue({ data: 5 });

      const result = await requestService.countPendingRequests();

      expect(mockedAxios.get).toHaveBeenCalledWith('/asset-requests/pending/count');
      expect(result).toBe(5);
    });
  });
});
