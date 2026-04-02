import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';
import { changeLanguage, getCurrentLanguage } from '../i18n';
import { useEffect } from 'react';

export default function LoginPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { login } = useAuth();
  const [currentLang, setCurrentLang] = useState(getCurrentLanguage());

  useEffect(() => {
    const lang = getCurrentLanguage();
    setCurrentLang(lang);
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = await authService.login({ email, password });
      login(data);
      navigate('/dashboard');
    } catch (err: unknown) {
      if (err && typeof err === 'object' && 'response' in err) {
        const axiosErr = err as { response?: { data?: { message?: string } } };
        setError(axiosErr.response?.data?.message || t('auth.invalidCredentials'));
      } else {
        setError(t('auth.invalidCredentials'));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLanguageChange = (lang: string) => {
    changeLanguage(lang);
    setCurrentLang(lang);
  };

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      {/* Language Switcher */}
      <div className="absolute top-4 right-4 flex gap-2">
        <button
          onClick={() => handleLanguageChange('vi')}
          className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
            currentLang === 'vi'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-200 text-gray-600 hover:bg-gray-300'
          }`}
        >
          Tiếng Việt
        </button>
        <button
          onClick={() => handleLanguageChange('en')}
          className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
            currentLang === 'en'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-200 text-gray-600 hover:bg-gray-300'
          }`}
        >
          English
        </button>
      </div>

      <div className="w-full max-w-md p-8 bg-white rounded-xl shadow-lg border border-gray-100">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">{t('auth.enterpriseTitle')}</h1>
          <p className="text-gray-500 mt-2">{t('auth.welcomeBack')}</p>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              {t('auth.email')}
            </label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition-colors"
              placeholder="admin@company.com"
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
              {t('auth.password')}
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition-colors"
              placeholder={t('auth.password')}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2.5 px-4 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? '...' : t('auth.signIn')}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-500">
          {t('auth.dontHaveAccount')}{' '}
          <Link to="/register" className="text-indigo-600 hover:text-indigo-700 font-medium">
            {t('auth.signUp')}
          </Link>
        </p>
      </div>
    </div>
  );
}
