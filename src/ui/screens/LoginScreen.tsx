import React, { useState } from 'react';
import { useTheme } from '../theme/ThemeContext';
import { AppTopBar } from '../components/AppTopBar';
import { AppDialog } from '../components/AppDialog';
import { apiLogin } from '../../data/remote/mockData';
import { Mail, Lock, Eye, EyeOff, AlertCircle, Info, Sparkles } from 'lucide-react';

interface LoginScreenProps {
  onNavigateBack: () => void;
  onNavigateToRegister: () => void;
  onNavigateToHome: () => void;
  onShowMessage: (msg: string) => void;
}

export const LoginScreen: React.FC<LoginScreenProps> = ({
  onNavigateBack,
  onNavigateToRegister,
  onNavigateToHome,
  onShowMessage,
}) => {
  const { t } = useTheme();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successDisplayName, setSuccessDisplayName] = useState<string | null>(null);

  const fillDemoAccount = () => {
    setEmail('demo@compose.dev');
    setPassword('password123');
    setEmailError(null);
    setPasswordError(null);
    setSubmitError(null);
  };

  const validate = (): boolean => {
    let isValid = true;
    setEmailError(null);
    setPasswordError(null);
    setSubmitError(null);

    const emailTrimmed = email.trim();
    if (!emailTrimmed) {
      setEmailError(t('login_email_required'));
      isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailTrimmed)) {
      setEmailError(t('login_email_invalid'));
      isValid = false;
    }

    if (!password) {
      setPasswordError(t('login_password_required'));
      isValid = false;
    } else if (password.length < 6) {
      setPasswordError(t('login_password_invalid'));
      isValid = false;
    }

    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setIsSubmitting(true);
    setSubmitError(null);

    try {
      const session = await apiLogin(email.trim(), password);
      onShowMessage(t('login_success_toast'));
      setSuccessDisplayName(session.displayName);
    } catch (err: any) {
      if (err.message === 'UNAUTHORIZED' || err.message === 'INVALID_CREDENTIALS') {
        setSubmitError(t('error_unauthorized'));
      } else {
        setSubmitError(t('error_server_generic'));
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F8FAFC] dark:bg-[#020617] flex flex-col">
      <AppTopBar title={t('login_title')} onBackClick={onNavigateBack} />

      <main className="flex-1 max-w-lg w-full mx-auto p-4 sm:p-6 flex flex-col justify-center">
        <div className="space-y-6">
          {/* Header & Slogan */}
          <div className="text-center space-y-2">
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 dark:text-white">
              {t('login_welcome_title')}
            </h2>
            <p className="text-sm font-medium text-[#3D5AFE] dark:text-[#B8C4FF]">
              {t('login_slogan')}
            </p>
          </div>

          {/* Demo account helper card */}
          <div className="p-4 rounded-2xl bg-indigo-50/80 dark:bg-[#2539B8]/20 border border-indigo-200/80 dark:border-indigo-800/60 flex items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-xl bg-[#3D5AFE] text-white flex items-center justify-center shrink-0">
                <Info className="w-5 h-5" />
              </div>
              <div className="text-xs space-y-0.5">
                <span className="font-semibold text-slate-800 dark:text-slate-200">
                  {t('login_demo_account_title')}
                </span>
                <p className="text-slate-600 dark:text-slate-400 font-mono">
                  {t('login_demo_account_body')}
                </p>
              </div>
            </div>
            <button
              type="button"
              onClick={fillDemoAccount}
              className="text-xs font-semibold px-3 py-1.5 rounded-lg bg-white dark:bg-slate-800 text-[#3D5AFE] dark:text-[#B8C4FF] shadow-2xs hover:bg-[#3D5AFE] hover:text-white dark:hover:bg-[#B8C4FF] dark:hover:text-slate-900 transition-all shrink-0 cursor-pointer"
            >
              Fill Demo
            </button>
          </div>

          {/* Error Banner */}
          {submitError && (
            <div className="p-4 rounded-2xl bg-rose-50 dark:bg-rose-950/40 border border-rose-200 dark:border-rose-900/60 flex items-start gap-3 text-rose-800 dark:text-rose-300">
              <AlertCircle className="w-5 h-5 shrink-0 mt-0.5 text-rose-600 dark:text-rose-400" />
              <div className="text-sm">
                <strong className="block font-semibold">{t('login_error_title')}</strong>
                <p>{submitError}</p>
              </div>
            </div>
          )}

          {/* Form Card */}
          <form
            onSubmit={handleSubmit}
            className="bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 rounded-3xl p-6 sm:p-8 shadow-xl shadow-slate-200/40 dark:shadow-none space-y-5"
          >
            <div>
              <h3 className="text-lg font-bold text-slate-900 dark:text-white">
                {t('login_form_title')}
              </h3>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">
                {t('login_form_description')}
              </p>
            </div>

            {/* Email Field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                {t('login_email_label')}
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Mail className="w-4 h-4" />
                </div>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                    if (emailError) setEmailError(null);
                  }}
                  placeholder="demo@compose.dev"
                  className={`w-full pl-10 pr-4 py-2.5 rounded-xl border text-sm bg-slate-50/50 dark:bg-slate-900/50 text-slate-900 dark:text-white placeholder:text-slate-400 outline-none transition-all ${
                    emailError
                      ? 'border-rose-500 ring-2 ring-rose-500/20'
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#3D5AFE] dark:focus:border-[#B8C4FF] focus:ring-2 focus:ring-[#3D5AFE]/20'
                  }`}
                />
              </div>
              {emailError && (
                <p className="text-xs text-rose-500 font-medium pl-1">{emailError}</p>
              )}
            </div>

            {/* Password Field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                {t('login_password_label')}
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-4 h-4" />
                </div>
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => {
                    setPassword(e.target.value);
                    if (passwordError) setPasswordError(null);
                  }}
                  placeholder="••••••••"
                  className={`w-full pl-10 pr-10 py-2.5 rounded-xl border text-sm bg-slate-50/50 dark:bg-slate-900/50 text-slate-900 dark:text-white placeholder:text-slate-400 outline-none transition-all ${
                    passwordError
                      ? 'border-rose-500 ring-2 ring-rose-500/20'
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#3D5AFE] dark:focus:border-[#B8C4FF] focus:ring-2 focus:ring-[#3D5AFE]/20'
                  }`}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
              {passwordError && (
                <p className="text-xs text-rose-500 font-medium pl-1">{passwordError}</p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-3 px-4 rounded-xl font-semibold text-sm bg-[#3D5AFE] hover:bg-[#324ad8] text-white shadow-md shadow-[#3D5AFE]/20 transition-all flex items-center justify-center gap-2 cursor-pointer disabled:opacity-60"
            >
              {isSubmitting ? (
                <>
                  <div className="w-4 h-4 border-2 border-white/40 border-t-white rounded-full animate-spin" />
                  <span>{t('common_loading')}</span>
                </>
              ) : (
                <span>{t('login_submit')}</span>
              )}
            </button>

            {/* Secondary Action */}
            <div className="text-center pt-2">
              <button
                type="button"
                onClick={onNavigateToRegister}
                className="text-xs font-medium text-slate-600 dark:text-slate-400 hover:text-[#3D5AFE] dark:hover:text-[#B8C4FF] transition-colors"
              >
                {t('login_open_register')}
              </button>
            </div>
          </form>
        </div>
      </main>

      {/* Success Dialog */}
      <AppDialog
        isOpen={!!successDisplayName}
        title={t('login_success_title')}
        message={t('login_success_message', successDisplayName || '')}
        confirmText={t('login_success_confirm')}
        onConfirm={() => {
          setSuccessDisplayName(null);
          onNavigateToHome();
        }}
        tone="success"
      />
    </div>
  );
};
