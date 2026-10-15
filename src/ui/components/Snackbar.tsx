import React, { useEffect } from 'react';
import { Info, CheckCircle2, X } from 'lucide-react';

interface SnackbarProps {
  message: string | null;
  onDismiss: () => void;
  duration?: number;
}

export const Snackbar: React.FC<SnackbarProps> = ({
  message,
  onDismiss,
  duration = 3200,
}) => {
  useEffect(() => {
    if (!message) return;
    const timer = setTimeout(() => {
      onDismiss();
    }, duration);
    return () => clearTimeout(timer);
  }, [message, duration, onDismiss]);

  if (!message) return null;

  return (
    <div className="fixed bottom-20 md:bottom-8 right-4 left-4 md:left-auto md:max-w-md z-50 flex items-center justify-between gap-3 px-4 py-3 rounded-2xl bg-slate-900/90 dark:bg-slate-100/95 text-white dark:text-slate-900 shadow-2xl backdrop-blur-md border border-slate-700/50 dark:border-slate-300 animate-in slide-in-from-bottom-4 duration-200">
      <div className="flex items-center gap-2.5 min-w-0">
        <CheckCircle2 className="w-5 h-5 text-emerald-400 dark:text-emerald-600 shrink-0" />
        <span className="text-sm font-medium leading-snug truncate">
          {message}
        </span>
      </div>
      <button
        onClick={onDismiss}
        className="p-1 rounded-lg hover:bg-white/10 dark:hover:bg-slate-900/10 transition-colors"
      >
        <X className="w-4 h-4" />
      </button>
    </div>
  );
};
