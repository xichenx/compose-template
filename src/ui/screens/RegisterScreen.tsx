import React, { useState } from 'react';
import { useTheme } from '../theme/ThemeContext';
import { AppTopBar } from '../components/AppTopBar';
import { AppDialog } from '../components/AppDialog';
import { apiRegister } from '../../data/remote/mockData';
import { User, Mail, Lock, Eye, EyeOff, AlertCircle, Info } from 'lucide-react';

interface RegisterScreenProps {
  onNavigateBack: () => void;
  onNavigateToHome: () => void;
  onShowMessage: (msg: string) => void;
}

export const RegisterScreen: React.FC<RegisterScreenProps> = ({
  onNavigateBack,
  onNavigateToHome,
  onShowMessage,
}) => {
  const { t } = useTheme();

  const [displayName, setDisplayName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [displayNameError, setDisplayNameError] = useState<string | null>(null);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [confirmPasswordError, setConfirmPasswordError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successDisplayName, setSuccessDisplayName] = useState<string | null>(null);

  const validate = (): boolean => {
    let isValid = true;
    setDisplayNameError(null);
    setEmailError(null);
    setPasswordError(null);
    setConfirmPasswordError(null);
    setSubmitError(null);

    if (!displayName.trim()) {
      setDisplayNameError(t('register_name_required'));
      isValid = false;
    }

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

    if (!confirmPassword) {
      setConfirmPasswordError(t('register_confirm_password_required'));
      isValid = false;
    } else if (password !== confirmPassword) {
      setConfirmPasswordError(t('register_confirm_password_not_match'));
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
      const session = await apiRegister(displayName.trim(), email.trim(), password);
      onShowMessage(t('register_success_toast'));
      setSuccessDisplayName(session.displayName);
    } catch (err: any) {
      if (err.message === 'EMAIL_EXISTS') {
        setEmailError('This email is already registered / 该邮箱已被注册');
      } else if (err.message === 'NAME_EMPTY') {
        setDisplayNameError(t('register_name_required'));
      } else if (err.message === 'PASSWORD_TOO_SHORT') {
        setPasswordError(t('login_password_invalid'));
      } else {
        setSubmitError(t('error_server_generic'));
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F8FAFC] dark:bg-[#020617] flex flex-col">
      <AppTopBar title={t('register_title')} onBackClick={onNavigateBack} />

      <main className="flex-1 max-w-lg w-full mx-auto p-4 sm:p-6 flex flex-col justify-center">
        <div className="space-y-6">
          {/* Header & Slogan */}
          <div className="text-center space-y-2">
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 dark:text-white">
              {t('register_welcome_title')}
            </h2>
            <p className="text-sm font-medium text-[#7C3AED] dark:text-[#D8B4FE]">
              {t('register_slogan')}
            </p>
          </div>

          {/* Demo hint banner */}
          <div className="p-4 rounded-2xl bg-purple-50/80 dark:bg-[#7C3AED]/20 border border-purple-200/80 dark:border-purple-800/60 flex items-start gap-3">
            <div className="w-8 h-8 rounded-xl bg-[#7C3AED] text-white flex items-center justify-center shrink-0 mt-0.5">
              <Info className="w-4 h-4" />
            </div>
            <div className="text-xs space-y-0.5">
              <span className="font-semibold text-slate-800 dark:text-slate-200">
                {t('register_hint_title')}
              </span>
              <p className="text-slate-600 dark:text-slate-400">
                {t('register_hint_body')}
              </p>
            </div>
          </div>

          {/* Error Banner */}
          {submitError && (
            <div className="p-4 rounded-2xl bg-rose-50 dark:bg-rose-950/40 border border-rose-200 dark:border-rose-900/60 flex items-start gap-3 text-rose-800 dark:text-rose-300">
              <AlertCircle className="w-5 h-5 shrink-0 mt-0.5 text-rose-600 dark:text-rose-400" />
              <div className="text-sm">
                <strong className="block font-semibold">{t('register_error_title')}</strong>
                <p>{submitError}</p>
              </div>
            </div>
          )}

          {/* Form Card */}
          <form
            onSubmit={handleSubmit}
            className="bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 rounded-3xl p-6 sm:p-8 shadow-xl shadow-slate-200/40 dark:shadow-none space-y-4"
          >
            <div>
              <h3 className="text-lg font-bold text-slate-900 dark:text-white">
                {t('register_form_title')}
              </h3>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">
                {t('register_form_description')}
              </p>
            </div>

            {/* Nickname Field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                {t('register_name_label')}
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <User className="w-4 h-4" />
                </div>
                <input
                  type="text"
                  value={displayName}
                  onChange={(e) => {
                    setDisplayName(e.target.value);
                    if (displayNameError) setDisplayNameError(null);
                  }}
                  placeholder="e.g. Alex"
                  className={`w-full pl-10 pr-4 py-2.5 rounded-xl border text-sm bg-slate-50/50 dark:bg-slate-900/50 text-slate-900 dark:text-white placeholder:text-slate-400 outline-none transition-all ${
                    displayNameError
                      ? 'border-rose-500 ring-2 ring-rose-500/20'
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#7C3AED] dark:focus:border-[#D8B4FE] focus:ring-2 focus:ring-[#7C3AED]/20'
                  }`}
                />
              </div>
              {displayNameError && (
                <p className="text-xs text-rose-500 font-medium pl-1">{displayNameError}</p>
              )}
            </div>

            {/* Email Field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                {t('register_email_label')}
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
                  placeholder="alex@example.com"
                  className={`w-full pl-10 pr-4 py-2.5 rounded-xl border text-sm bg-slate-50/50 dark:bg-slate-900/50 text-slate-900 dark:text-white placeholder:text-slate-400 outline-none transition-all ${
                    emailError
                      ? 'border-rose-500 ring-2 ring-rose-500/20'
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#7C3AED] dark:focus:border-[#D8B4FE] focus:ring-2 focus:ring-[#7C3AED]/20'
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
                {t('register_password_label')}
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
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#7C3AED] dark:focus:border-[#D8B4FE] focus:ring-2 focus:ring-[#7C3AED]/20'
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

            {/* Confirm Password Field */}
            <div className="space-y-1.5">
              <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                {t('register_confirm_password_label')}
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-4 h-4" />
                </div>
                <input
                  type={showConfirmPassword ? 'text' : 'password'}
                  value={confirmPassword}
                  onChange={(e) => {
                    setConfirmPassword(e.target.value);
                    if (confirmPasswordError) setConfirmPasswordError(null);
                  }}
                  placeholder="••••••••"
                  className={`w-full pl-10 pr-10 py-2.5 rounded-xl border text-sm bg-slate-50/50 dark:bg-slate-900/50 text-slate-900 dark:text-white placeholder:text-slate-400 outline-none transition-all ${
                    confirmPasswordError
                      ? 'border-rose-500 ring-2 ring-rose-500/20'
                      : 'border-slate-200 dark:border-slate-700 focus:border-[#7C3AED] dark:focus:border-[#D8B4FE] focus:ring-2 focus:ring-[#7C3AED]/20'
                  }`}
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                >
                  {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
              {confirmPasswordError && (
                <p className="text-xs text-rose-500 font-medium pl-1">{confirmPasswordError}</p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full mt-2 py-3 px-4 rounded-xl font-semibold text-sm bg-[#7C3AED] hover:bg-[#6d2fd0] text-white shadow-md shadow-[#7C3AED]/20 transition-all flex items-center justify-center gap-2 cursor-pointer disabled:opacity-60"
            >
              {isSubmitting ? (
                <>
                  <div className="w-4 h-4 border-2 border-white/40 border-t-white rounded-full animate-spin" />
                  <span>{t('common_loading')}</span>
                </>
              ) : (
                <span>{t('register_submit')}</span>
              )}
            </button>
          </form>
        </div>
      </main>

      {/* Success Dialog */}
      <AppDialog
        isOpen={!!successDisplayName}
        title={t('register_success_title')}
        message={t('register_success_message', successDisplayName || '')}
        confirmText={t('register_success_confirm')}
        onConfirm={() => {
          setSuccessDisplayName(null);
          onNavigateToHome();
        }}
        tone="success"
      />
    </div>
  );
};
