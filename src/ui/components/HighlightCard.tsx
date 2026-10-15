import React from 'react';
import { ScaffoldHighlight } from '../../domain/models';
import { ChevronRight } from 'lucide-react';

interface HighlightCardProps {
  item: ScaffoldHighlight;
  onClick: (id: string) => void;
}

export const HighlightCard: React.FC<HighlightCardProps> = ({ item, onClick }) => {
  return (
    <div
      onClick={() => onClick(item.id)}
      role="button"
      tabIndex={0}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          onClick(item.id);
        }
      }}
      className="group w-full text-left p-4 sm:p-5 rounded-2xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs hover:shadow-md hover:border-[#3D5AFE]/40 dark:hover:border-[#B8C4FF]/40 transition-all cursor-pointer relative overflow-hidden"
    >
      <div className="flex items-start justify-between gap-3">
        <div className="space-y-1.5 flex-1">
          <span className="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold tracking-wide uppercase bg-[#E0E7FF] dark:bg-[#2539B8]/40 text-[#3D5AFE] dark:text-[#B8C4FF]">
            {item.stage}
          </span>
          <h3 className="text-base font-semibold text-slate-900 dark:text-white group-hover:text-[#3D5AFE] dark:group-hover:text-[#B8C4FF] transition-colors">
            {item.title}
          </h3>
          <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">
            {item.description}
          </p>
        </div>
        <div className="p-2 rounded-xl bg-slate-100 dark:bg-slate-800 text-slate-400 group-hover:text-[#3D5AFE] dark:group-hover:text-[#B8C4FF] group-hover:translate-x-0.5 transition-all">
          <ChevronRight className="w-4 h-4" />
        </div>
      </div>
    </div>
  );
};
