'use client';

import { useEffect } from 'react';

const Layout = ({ children }: any) => {
  useEffect(() => {
    // 여기에 Pull to Refresh 관련 이벤트 리스너 등록
    const handlePullToRefresh = () => {
      // 새로고침 로직을 여기에 추가
      window.location.reload();
    };

    document.addEventListener('touchmove', handlePullToRefresh, {
      passive: false,
    });

    return () => {
      // Cleanup 작업
      document.removeEventListener('touchmove', handlePullToRefresh);
    };
  }, []);

  return <div className="min-h-full w-full flex items-center justify-center">{children}</div>;
};

export default Layout;
