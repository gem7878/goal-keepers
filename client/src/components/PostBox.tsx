'use client';

import Image from 'next/image';
import React, { useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faShare, faPlus } from '@fortawesome/free-solid-svg-icons';

interface postContentTypes {
  content: string;
  createdAt: string;
  like: boolean;
  likeCnt: number;
  nickname: string;
  contentId: number;
}

interface postTypes {
  content: postContentTypes;
  goalDescription: string;
  goalId: number;
  goalImageUrl: null | string;
  goalTitle: string;
  goalshareCnt: number;
  postId: number;
  share: boolean;
  cheer: boolean;
  myPost: false;
  nickname: string;
  postCheerCnt: number;
  privated: boolean;
}

const PostBox: React.FC<{
  data: postTypes;
  index: number;
  focusNum: number | null;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
  onCheerPost: (index: number) => void;
  onShareGoal: (index: number) => void;
  onGetShareData: (index: number) => void;
}> = ({
  data,
  index,
  focusNum,
  setFocusNum,
  onCheerPost,
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
        <div className="w-full h-3/4 relative">
          <Image
            src={data.goalImageUrl === null ? Image1 : data.goalImageUrl}
            alt=""
            fill
            style={{
              objectFit: 'cover',
              zIndex: 1,
            }}
          ></Image>
        </div>

        <h3 className="text-center px-1 max-w-fit mx-4	text-white	font-bold absolute top-1/3 -translate-y-1/3 z-10 bg-black text-ellipsis	">
          {data.goalTitle}
          {data.goalTitle.length > 22 && '...'}
        </h3>
        <ul ref={likeRef} className="flex w-full justify-center	gap-2">
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faHeart}
              onClick={() => onCheerPost(index)}
              className="text-orange-500"
            />
            <label
              className={`text-xs	font-semibold	
              ${data.cheer ? 'text-orange-400' : 'text-gray-500'}
              `}
            >
              {data.postCheerCnt}
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
              {data.goalshareCnt}
            </label>
          </li>
        </ul>
      </div>
      <div className="w-1/2	flex flex-col justify-center">
        <li
          className={`text-gray-600 text-sm bg-orange-100 my-2 py-1 rounded-md px-2 flex justify-between`}
        >
          <span>{data.content.content}</span>
        </li>
        <FontAwesomeIcon icon={faPlus} className="text-gray-600 mt-3" />
        {/* <div className="flex flex-col">
          <span className="text-center h-5">.</span>
          <span className="text-center h-5">.</span>
          <span className="text-center h-5">.</span>
        </div> */}
      </div>
    </article>
  );
};

export default PostBox;
