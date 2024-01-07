'use client';

import Image from 'next/image';
import React, { useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/aurora.jpg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faShare } from '@fortawesome/free-solid-svg-icons';

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

const PostBox: React.FC<{
  data: postDataTypes;
  index: number;
  focusNum: number | null;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
  onLikePost: (index: number) => void;
  onShareGoal: (index: number) => void;
  onGetShareData: (index: number) => void;
}> = ({
  data,
  index,
  focusNum,
  setFocusNum,
  onLikePost,
  onShareGoal,
  onGetShareData,
}) => {
  const likeRef = useRef<HTMLUListElement>(null);

  const handleFocus = (e: { target: any }) => {
    if (!likeRef.current?.contains(e.target)) {
      if (focusNum === index) {
        // setFocusNum(null);
      } else {
        setFocusNum(index);
      }
    }
  };
  return (
    <article
      onClick={(e) => handleFocus(e)}
      className="h-44
      flex
      justify-between
      p-3
      mb-4
      border
      rounded-md
      duration-100
      w-11/12
      inset-x-0
      mx-auto
       "
    >
      <div className="w-1/2 h-full relative px-2 z-0 flex flex-col	justify-between items-center">
        <Image
          // src={data.image}
          src={Image1}
          alt=""
          style={{
            width: '100%',
            height: '75%',
            objectFit: 'cover',
            zIndex: 1,
          }}
        ></Image>
        <h3 className="text-center px-1 max-w-fit mx-4	text-white	font-bold absolute top-1/3 -translate-y-1/3 z-10 bg-black text-ellipsis	">
          {data.goalTitle}
          {data.goalTitle.length > 22 && '...'}
        </h3>
        <ul ref={likeRef} className="flex w-full justify-center	gap-2">
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faHeart}
              onClick={() => onLikePost(index)}
              className="text-orange-500"
            />
            <label
              className={`text-xs	${
                data.like ? 'text-orange-400' : 'text-gray-500'
              }`}
            >
              {data.likeCnt}
            </label>
          </li>
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faShare}
              onClick={() => {
                data.share ? onGetShareData(data.goalId) : onShareGoal(index);
              }}
              className="text-gray-600"
            />
            <label
              className={`text-xs	${
                data.share ? 'text-orange-400' : 'text-gray-500'
              }`}
            >
              {data.shareCnt}
            </label>
          </li>
        </ul>
      </div>
      <div className="w-1/2	flex flex-col justify-between">
        <div className="mt-2">
          <h3 className="font-bold	">{data.title}</h3>
          <p className="text-sm	">{data.content}</p>
        </div>
        <label className="text-xs	w-full text-right	">
          {data.updatedAt.slice(0, 10)}
        </label>
      </div>
    </article>
  );
};

export default PostBox;
