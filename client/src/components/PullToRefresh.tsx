'use client';

import React, { useEffect, useRef, useState } from 'react';
import { CircularProgress } from '@mui/material';
import { useGesture } from '@use-gesture/react';

const PullToRefresh = ({ onRefresh, children }: any) => {
  const bind = useGesture({
    onDrag: ({ movement: [mx, my], down }) => {
      // Drag 이벤트 핸들러
      if (!down && my > 50) {
        // 드래그를 끝냈을 때, 세로로 50px 이상 드래그된 경우 리프레시 실행
        onRefresh();
      }
    },
  });

  return <div {...bind()}>{children}</div>;
};

export default PullToRefresh;
