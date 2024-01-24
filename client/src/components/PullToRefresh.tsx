'use client';

import React, { useEffect, useRef, useState } from 'react';
import { CircularProgress } from '@mui/material';

const PullToRefresh = ({ el }: any) => {
  const [refreshing, setRefreshing] = useState(false);
  const [startY, setStartY] = useState(0);


  const handleRefresh = () => {
    window.location.reload();
  };

  useEffect(() => {
    function handleTouchStart(event: any) {
      setStartY(event.touches[0].clientY);
    }

    function handleTouchMove(event: any) {
      const moveY = event.touches[0].clientY;
      const pullDistance = moveY - startY;

      if (pullDistance > 0) {
        event.preventDefault();

        if (pullDistance > 80) {
          el.current.style.transform = 'translate(0, 40px)';
          el.current.style.transition = '0.3s';
          setRefreshing(true);
        }
      }
    }

    function handleTouchEnd() {
      if (refreshing) {
        // window.location.reload();
        setTimeout(() => {
          setRefreshing(false);
          el.current.style.transform = 'translate(0,0)';
        }, 1000);
      }
    }

    document.addEventListener('touchstart', handleTouchStart);
    document.addEventListener('touchmove', handleTouchMove);
    document.addEventListener('touchend', handleTouchEnd);

    return () => {
      document.removeEventListener('touchstart', handleTouchStart);
      document.removeEventListener('touchmove', handleTouchMove);
      document.removeEventListener('touchend', handleTouchEnd);
    };
  }, [refreshing, startY, el]);

  return (
    <div>
      <section>
        {refreshing ? <CircularProgress color="inherit" /> : ''}
      </section>
    </div>
  );
};

export default PullToRefresh;
