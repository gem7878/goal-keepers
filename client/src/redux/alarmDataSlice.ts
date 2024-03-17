import { createSlice } from '@reduxjs/toolkit';

const alarmDataSlice = createSlice({
  name: 'alarmData',
  initialState: {
    targetId: null,
    targetPage: null,
    commentId: null,
    commentPage: null,
  },
  reducers: {
    setTargetId: (state, action) => {
      state.targetId = action.payload;
    },
    setTargetPage: (state, action) => {
      state.targetPage = action.payload;
    },
    setCommentId: (state, action) => {
      state.commentId = action.payload;
    },
    setCommentPage: (state, action) => {
      state.commentPage = action.payload;
    },
  },
});

export const {
  setTargetId,
  setTargetPage,
  setCommentId,
  setCommentPage,
} = alarmDataSlice.actions;
export const selectAlarmData = (state: any) => state.alarmData; // 선택자(selectors) 추가
export default alarmDataSlice.reducer;
