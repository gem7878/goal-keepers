import { createSlice } from '@reduxjs/toolkit';

const renderSlice = createSlice({
  name: 'render',
  initialState: {
    goalBoolean: false,
    postBoolean: null,
    commentBoolean: false,
    goalId: null,
    contentBoolean: false,
  },
  reducers: {
    setStateGoal: (state, action) => {
      state.goalBoolean = action.payload;
    },
    setStatePost: (state, action) => {
      state.postBoolean = action.payload;
    },
    setStateComment: (state, action) => {
      state.commentBoolean = action.payload;
    },
    setCreateButton: (state, action) => {
      state.goalId = action.payload;
    },
    setShareGoal: (state, action) => {
      state.goalId = action.payload;
    },
    setStateContent: (state, action) => {
      state.contentBoolean = action.payload;
    },
  },
});

export const {
  setStateGoal,
  setCreateButton,
  setStatePost,
  setStateComment,
  setShareGoal,
  setStateContent,
} = renderSlice.actions;
export const selectRender = (state: any) => state.render; // 선택자(selectors) 추가
export default renderSlice.reducer;
