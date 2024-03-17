import { configureStore } from '@reduxjs/toolkit';
import goalDataSlice from './goalDataSlice';
import renderSlice from './renderSlice';
import alarmDataSlice from './alarmDataSlice';

export const store = configureStore({
  reducer: {
    goalData: goalDataSlice,
    render: renderSlice,
    alarmData: alarmDataSlice,
  },
});
