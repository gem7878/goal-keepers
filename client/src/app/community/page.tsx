'use client';

import { CreateButton, PostBox, PostBoxDetail } from '@/components/index.js';
import React, { useEffect, useRef, useState } from 'react';
import { handleGetPostAll, handleLikePost } from './actions';
import { handleGetUserInfo } from '../actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStatePost } from '@/redux/renderSlice';

interface postTypes {
  postId: number;
  nickname: string;
  title: string;
  content: string;
  updatedAt: string;
  likeCnt: number;
  goalId: number;
  goalTitle: string;
  goalDescription: string;
  goalImageUrl: string;
  shareCnt: number;
  like: boolean;
  share: false;
}

const Community = () => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [postData, setPostData] = useState<postTypes[]>([]);
  const [nickname, setNickname] = useState('');

  const dispatch = useDispatch();

  useEffect(() => {
    onGetUserInfo();
  }, []);
  const reduxPostData = useSelector(selectRender);

  useEffect(() => {
    handleAllPost();
  }, [reduxPostData.postBoolean]);

  const handleAllPost = async () => {
    const form = { pageNum: 1 };
    await handleGetPostAll(form)
      .then((response) => {
        setPostData(response.content);
      })
      .catch((error) => console.log(error));
  };

  const onGetUserInfo = async () => {
    await handleGetUserInfo()
      .then((response) => {
        setNickname(response.nickname);
      })
      .catch((error) => console.log(error));
  };

  const onLikePost = async (index: number) => {
    await handleLikePost(postData[index].postId)
      .then((response) => {
        if (response.success) {
            dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };

  return (
    <div className="w-full	h-full pt-[80px]">
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11	fixed top-7 bg-white ">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <button>search</button>
      </header>
      <section className="z-0 h-full overflow-y-scroll w-full">
        {postData.map((data, index) => {
          if (focusNum === index) {
            return (
              <PostBoxDetail
                data={data}
                key={index}
                myNickname={nickname}
                setFocusNum={setFocusNum}
                index={index}
                onLikePost={onLikePost}
              ></PostBoxDetail>
            );
          } else {
            return (
              <PostBox
                data={data}
                key={index}
                index={index}
                focusNum={focusNum}
                setFocusNum={setFocusNum}
                onLikePost={onLikePost}
              ></PostBox>
            );
          }
        })}
      </section>
      <CreateButton></CreateButton>
    </div>
  );
};

export default Community;
