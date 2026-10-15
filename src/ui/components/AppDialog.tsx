import React from 'react';
import { CheckCircle, AlertTriangle, Info, X } from 'lucide-react';

export type AppDialogTone = 'success' | 'info' | 'error';

interface AppDialogProps {
  isOpen: boolean;
  title: string;
  message: string;
  confirmText: string;
  onConfirm: () => void;
  tone?: AppDialogTone;
}

export const AppDialog: React.FC<AppDialogProps> = ({
  isOpen,
  title,
  message,
  confirmText,
  onConfirm,
  tone = 'success',
}) => {
  if (!isOpen) return null;

  const toneConfig = {
    success: {
      icon: CheckCircle,
      iconColor: 'text-emerald-500',
      iconBg: 'bg-emerald-50 dark:bg-emerald-950/50',
      buttonBg: 'bg-[#3D5AFE] hover:bg-[#324ad8] dark:bg-[#3D5AFE] dark:hover:bg-[#4d69ff] text-white',
    },
    info: {
      icon: Info,
      iconColor: 'text-blue-500',
      iconBg: 'bg-blue-50 dark:bg-blue-950/50',
      buttonBg: 'bg-blue-600 hover:bg-blue-700 text-white',
    },
    error: {
      icon: AlertTriangle,
      iconColor: 'text-rose-500',
      iconBg: 'bg-rose-50 dark:bg-rose-950/50',
      buttonBg: 'bg-rose-600 hover:bg-rose-700 text-white',
    },
  }[tone];

  const IconComponent = toneConfig.icon;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-xs animate-in fade-in duration-200">
      <div
        role="dialog"
        aria-modal="true"
        className="w-full max-w-sm rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200 dark:border-slate-800 p-6 shadow-2xl space-y-4 animate-in zoom-in-95 duration-200 text-center"
      >
        <div className={`mx-auto w-14 h-14 rounded-2xl ${toneConfig.iconBg} flex items-center justify-center`}>
          <IconComponent className={`w-8 h-8 ${toneConfig.iconColor}`} />
        </div>

        <div className="space-y-1.5">
          <h3 className="text-lg font-bold text-slate-900 dark:text-white">
            {title}
          </h3>
          <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">
            {message}
          </p>
        </div>

        <div className="pt-2">
          <button
            onClick={onConfirm}
            className={`w-full py-3 px-4 rounded-xl font-semibold text-sm transition-all shadow-sm ${toneConfig.buttonBg}`}
          >
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
};
