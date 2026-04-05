import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from '../services/api';
import authService from '../services/authService';

vi.mock('../services/api');

const mockedAxios = vi.mocked(axios);

describe('AuthService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('login', () => {
    it('should login successfully and return token', async () => {
      const loginData = { email: 'test@example.com', password: 'password123' };
      const mockResponse = {
        token: 'jwt-token-here',
        tokenType: 'Bearer',
        user: { id: '1', name: 'Test User', email: 'test@example.com', role: 'USER' },
      };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await authService.login(loginData);

      expect(mockedAxios.post).toHaveBeenCalledWith('/auth/login', loginData);
      expect(result).toEqual(mockResponse);
      expect(result.token).toBe('jwt-token-here');
    });

    it('should throw error on invalid credentials', async () => {
      const loginData = { email: 'wrong@example.com', password: 'wrongpass' };
      mockedAxios.post.mockRejectedValue(new Error('Invalid credentials'));

      await expect(authService.login(loginData)).rejects.toThrow('Invalid credentials');
    });
  });

  describe('register', () => {
    it('should register new user successfully', async () => {
      const registerData = {
        name: 'New User',
        email: 'new@example.com',
        password: 'password123',
      };
      const mockResponse = {
        id: 'new-id',
        name: 'New User',
        email: 'new@example.com',
        role: 'USER',
      };
      mockedAxios.post.mockResolvedValue({ data: mockResponse });

      const result = await authService.register(registerData);

      expect(mockedAxios.post).toHaveBeenCalledWith('/auth/register', registerData);
      expect(result.email).toBe('new@example.com');
    });

    it('should throw error on duplicate email', async () => {
      const registerData = {
        name: 'Test User',
        email: 'existing@example.com',
        password: 'password123',
      };
      mockedAxios.post.mockRejectedValue(new Error('Email already registered'));

      await expect(authService.register(registerData)).rejects.toThrow('Email already registered');
    });
  });

  describe('getProfile', () => {
    it('should fetch current user profile', async () => {
      const mockUser = {
        id: '1',
        name: 'Test User',
        email: 'test@example.com',
        role: 'USER',
      };
      mockedAxios.get.mockResolvedValue({ data: mockUser });

      const result = await authService.getProfile();

      expect(mockedAxios.get).toHaveBeenCalledWith('/auth/me');
      expect(result).toEqual(mockUser);
    });
  });
});
