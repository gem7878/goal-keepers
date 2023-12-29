'use client';

import Image from 'next/image';
import React, { useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/aurora.jpg';
import {
  handleDeletePost,
  handlePutPost,
} from '@/app/community/actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStatePost } from '@/redux/renderSlice';
import { CommentBox } from './index';

interface postDataTypes {
  content: string;
  goalDescription: string;
  goalId: number;
  goalImageUrl: string;
  goalTitle: string;
  like: boolean;
  likeCnt: number;
  nickname: string;
  postId: number;
  share: boolean;
  shareCnt: number;
  title: string;
  updatedAt: string;
}

const PostBoxDetail: React.FC<{
  data: postDataTypes;
  myNickname: string;
  index: number;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
  onLikePost: (index: number) => void;
}> = ({ data, myNickname, setFocusNum, index, onLikePost }) => {
  const [isPostEdit, setIsPostEdit] = useState(false);
  const [postTitle, setPostTitle] = useState(data.title);
  const [postContent, setPostContent] = useState(data.content);

  const likeRef = useRef<HTMLUListElement>(null);
  const reduxPostData = useSelector(selectRender);
  const dispatch = useDispatch();

  const onUpdatePost = async () => {
    const postData = {
      title: postTitle,
      content: postContent,
      goalId: data.goalId,
      postId: data.postId,
    };
    const confirm = window.confirm('포스트 수정을 완료하시겠습니까?');
    if (confirm) {
      await handlePutPost(postData)
        .then((response) => {
          if (response.success) {
            setIsPostEdit(false);
            dispatch(setStatePost(!reduxPostData.postBoolean));
          }
        })
        .catch((error) => console.log(error));
    }
  };
  const onDeletePost = async () => {
    const postData = {
      postId: data.postId,
    };
    const confirm = window.confirm('포스트를 삭제하시겠습니까?');
    if (confirm) {
      await handleDeletePost(postData)
        .then((response) => {
          setFocusNum(null);
          dispatch(setStatePost(!reduxPostData.postBoolean));
        })
        .catch((error) => console.log(error));
    }
  };

  return (
    <article
      className="h-3/4 flex-col p-3 mb-4 border rounded-md duration-100	
      w-11/12
      inset-x-0
      mx-auto"
    >
      <div className="w-full h-1/4 relative z-0 flex rounded-md	">
        <Image
          // src={data.image}
          src={Image1}
          alt=""
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
            // zIndex: 1,
            position: 'absolute',
            borderRadius: '5px',
          }}
        ></Image>
        <div className="w-full h-full bg-black absolute opacity-50"></div>
        {myNickname === data.nickname && (
          <div className="flex text-white absolute top-0 right-0 text-xs gap-2 m-2">
            {isPostEdit ? (
              <>
                <button onClick={() => setIsPostEdit(false)}>cancel</button>
                <button onClick={() => onUpdatePost()}>complete</button>
              </>
            ) : (
              <>
                <button onClick={() => setIsPostEdit(true)}>edit</button>
                <button onClick={() => onDeletePost()}>delete</button>
              </>
            )}
          </div>
        )}

        <h3 className="text-center px-1  mx-4	text-white	font-bold absolute top-1/4 -translate-y-1/3 z-10 text-ellipsis	">
          {data.goalTitle.length > 18
            ? data.goalTitle.slice(0, 18) + '...'
            : data.goalTitle}
        </h3>
        <p className="text-white w-5/6 absolute top-1/3 text-xs mt-2 mx-4">
          {data.goalDescription.length > 47
            ? data.goalDescription.slice(0, 47) + '...'
            : data.goalDescription}
        </p>
        <ul
          ref={likeRef}
          className="absolute right-0 bottom-0 mb-1 mr-3 flex justify-center	text-white gap-2"
        >
          <li className="flex items-center gap-1">
            <button
              onClick={() => {
                data.like || onLikePost(index);
              }}
            >
              🧡
            </button>
            <label
              className={`text-xs	${
                data.like ? 'text-orange-400' : 'text-gray-300'
              }`}
            >
              {data.likeCnt}
            </label>
          </li>
          <li className="flex items-center gap-1">
            <button>➕</button>
            <label
              className={`text-xs	${
                data.share ? 'text-orange-400' : 'text-gray-300'
              }`}
            >
              {data.shareCnt}
            </label>
          </li>
        </ul>
      </div>
      {isPostEdit ? (
        <div className="w-full h-[40%]	pt-2 flex flex-col">
          <input
            className="font-bold"
            value={postTitle}
            onChange={(e) => setPostTitle(e.target.value)}
          ></input>
          <h5 className="text-xs	w-full text-right	">
            {data.updatedAt.slice(0, 10)}
          </h5>
          <textarea
            className="text-sm h-3/5"
            defaultValue={postContent}
            onChange={(e) => setPostContent(e.target.value)}
          ></textarea>
        </div>
      ) : (
        <div className="w-full h-[40%]	pt-2 flex flex-col">
          <h3 className="font-bold">
            {postTitle.length > 20 ? postTitle.slice(0, 20) + '...' : postTitle}
          </h3>
          <h5 className="text-xs	w-full text-right	">
            {data.updatedAt.slice(0, 10)}
          </h5>
          <p className="text-sm	">{postContent}</p>
        </div>
      )}
      <CommentBox postId={data.postId} myNickname={myNickname}></CommentBox>
    </article>
  );
};

export default PostBoxDetail;
