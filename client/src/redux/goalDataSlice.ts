import { createSlice } from '@reduxjs/toolkit';

const goalDataSlice = createSlice({
  name: 'goalData',
  initialState: {
    title: null,
    description: null,
    startDate: null,
    endDate: null,
    imageUrl: null,
  },
  reducers: {
    setTitle: (state, action) => {
      state.title = action.payload;
    },
    setDescription: (state, action) => {
      state.description = action.payload;
    },
    setStartDate: (state, action) => {
      state.startDate = action.payload;
    },
    setEndDate: (state, action) => {
      state.endDate = action.payload;
    },
    setImageUrl: (state, action) => {
      state.imageUrl = action.payload;
    },
  },
});

export const {
  setTitle,
  setDescription,
  setStartDate,
  setEndDate,
  setImageUrl,
} = goalDataSlice.actions;
export const selectGoalData = (state: any) => state.goalData; // 선택자(selectors) 추가
export default goalDataSlice.reducer;
