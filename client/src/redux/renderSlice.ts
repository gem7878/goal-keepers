import { createSlice } from '@reduxjs/toolkit';

const renderSlice = createSlice({
  name: 'render',
  initialState: {
    goalBoolean: false,
    postBoolean: null,
    communityBoolean: false,
    commentBoolean: false,
    goalId: null,
    contentBoolean: false,
    privateBoolean: false,
    alarmBoolean: false,
    inquiryBoolean: false,
    alarmTargetBoolean: false,
    logOutBoolean: false,
  },
  reducers: {
    setStateGoal: (state, action) => {
      state.goalBoolean = action.payload;
    },
    setStatePost: (state, action) => {
      state.postBoolean = action.payload;
    },
    setStateCommunity: (state, action) => {
      state.communityBoolean = action.payload;
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
    setStatePrivate: (state, action) => {
      state.privateBoolean = action.payload;
    },
    setStateAlarm: (state, action) => {
      state.alarmBoolean = action.payload;
    },
    setStateInquiry: (state, action) => {
      state.inquiryBoolean = action.payload;
    },
    setStateAlarmTarget: (state, action) => {
      state.alarmBoolean = action.payload;
    },
    setStateLogOut: (state, action) => {
      state.logOutBoolean = action.payload;
    },
  },
});

export const {
  setStateGoal,
  setCreateButton,
  setStatePost,
  setStateCommunity,
  setStateComment,
  setShareGoal,
  setStateContent,
  setStatePrivate,
  setStateAlarm,
  setStateInquiry,
  setStateAlarmTarget,
  setStateLogOut,
} = renderSlice.actions;
export const selectRender = (state: any) => state.render; // 선택자(selectors) 추가
export default renderSlice.reducer;
