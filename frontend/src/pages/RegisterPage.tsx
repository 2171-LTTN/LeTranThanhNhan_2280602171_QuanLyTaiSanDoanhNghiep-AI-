import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { useTranslation } from 'react-i18next';
import { changeLanguage, getCurrentLanguage } from '../i18n';
import { useEffect } from 'react';

export default function RegisterPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [currentLang, setCurrentLang] = useState(getCurrentLanguage());

  useEffect(() => {
    setCurrentLang(getCurrentLanguage());
  }, []);

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await authService.register({ name, email, password });
      navigate('/login');
    } catch (err: unknown) {
      if (err && typeof err === 'object' && 'response' in err) {
        const axiosErr = err as { response?: { data?: { message?: string } } };
        setError(axiosErr.response?.data?.message || t('auth.registerFailed'));
      } else {
        setError(t('auth.registerFailed'));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLanguageChange = (lang: string) => {
    changeLanguage(lang);
    setCurrentLang(lang);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="absolute top-4 right-4 flex gap-2">
        <button
          onClick={() => handleLanguageChange('vi')}
          className={`px-3 py-1.5 text-xs font-medium rounded-lg ${currentLang === 'vi' ? 'bg-indigo-600 text-white' : 'bg-gray-200 text-gray-600 hover:bg-gray-300'}`}
        >
          Tiếng Việt
        </button>
        <button
          onClick={() => handleLanguageChange('en')}
          className={`px-3 py-1.5 text-xs font-medium rounded-lg ${currentLang === 'en' ? 'bg-indigo-600 text-white' : 'bg-gray-200 text-gray-600 hover:bg-gray-300'}`}
        >
          English
        </button>
      </div>

      <div className="w-full max-w-md p-8 bg-white rounded-xl shadow-lg border border-gray-100">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">{t('auth.createAccount')}</h1>
          <p className="text-gray-500 mt-2">{t('auth.enterpriseTitle')}</p>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">{t('auth.name')}</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="John Doe"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">{t('auth.email')}</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder="john@company.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">{t('auth.password')}</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={6}
              className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              placeholder={t('auth.password')}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2.5 px-4 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg disabled:opacity-50"
          >
            {loading ? '...' : t('auth.signUp')}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-gray-500">
          {t('auth.alreadyHaveAccount')}{' '}
          <Link to="/login" className="text-indigo-600 hover:text-indigo-700 font-medium">
            {t('auth.signIn')}
          </Link>
        </p>
      </div>
    </div>
  );
}
