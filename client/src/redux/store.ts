import { configureStore } from '@reduxjs/toolkit';
import counterReducer from './counterSlice';
import goalDataSlice from './goalDataSlice';

export const store = configureStore({
  reducer: {
    counter: counterReducer,
    goalData: goalDataSlice,
  },
});
