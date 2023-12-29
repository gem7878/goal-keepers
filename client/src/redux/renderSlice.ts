import { createSlice } from '@reduxjs/toolkit';

const renderSlice = createSlice({
  name: 'render',
  initialState: {
    boolean: false,
  },
  reducers: {
    setState: (state, action) => {
      state.boolean = action.payload;
    },
  },
});

export const { setState } = renderSlice.actions;
export const selectRender = (state: any) => state.render; // 선택자(selectors) 추가
export default renderSlice.reducer;
