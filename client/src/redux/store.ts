import { configureStore } from '@reduxjs/toolkit';
import goalDataSlice from './goalDataSlice';
import renderSlice from './renderSlice';

export const store = configureStore({
  reducer: {
    goalData: goalDataSlice,
    render: renderSlice,
  },
});
