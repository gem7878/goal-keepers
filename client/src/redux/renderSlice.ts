import { createSlice } from '@reduxjs/toolkit';

const renderSlice = createSlice({
  name: 'render',
  initialState: {
    goalBoolean: false,
    postBoolean: false,
    goalId: null,
  },
  reducers: {
    setStateGoal: (state, action) => {
      state.goalBoolean = action.payload;
    },
    setStatePost: (state, action) => {
      state.postBoolean = action.payload;
    },
    setCreateButton: (state, action) => {
      state.goalId = action.payload;
    },
  },
});

export const { setStateGoal, setCreateButton, setStatePost } =
  renderSlice.actions;
export const selectRender = (state: any) => state.render; // 선택자(selectors) 추가
export default renderSlice.reducer;
