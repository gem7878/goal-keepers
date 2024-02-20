'use client';

import React, { useState } from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faShare, faPlus } from '@fortawesome/free-solid-svg-icons';
import { faHeart as farHeart } from '@fortawesome/free-regular-svg-icons';
import { handleLikeContent } from '@/app/community/actions';
import { useDispatch, useSelector } from 'react-redux';
import {
  selectRender,
  setStateContent,
  setStatePost,
} from '@/redux/renderSlice';
interface communityContentList {
  content: string;
  contentId: number;
  createdAt: string;
  goalDescription: string;
  goalId: number;
  goalImageUrl: null | string;
  goalTitle: string;
  like: boolean;
  likeCnt: number;
  nickname: string;
}

const ContentBox: React.FC<{
  list: communityContentList;
  index: number;
}> = ({ list, index }) => {
  const reduxContentData = useSelector(selectRender);
  const dispatch = useDispatch();

  const [isLike, setIsLike] = useState(list.like);

  const onLikeContent = async () => {
    setIsLike(!isLike);
    await handleLikeContent(list.contentId)
      .then((response) => {
        if (response.success) {
          dispatch(setStateContent(!reduxContentData.contentBoolean));
        }
      })
      .catch((error) => console.log(error));
  };

  return (
    <li
      key={index}
      className={`text-gray-600 bg-neutral-100 py-1 rounded-md px-2 drop-shadow-lg h-12 mb-3 content-element`}
    >
      <div className="w-full flex items-center justify-between">
        <div className="rounded-full h-4 bg-orange-200 px-2 ">
          <p className="text-gray-600 text-[11px] leading-4 ">
            {list.nickname}
          </p>
        </div>
        <span className="text-[10px] text-slate-400">
          {list.createdAt.slice(0, 10)}
        </span>
      </div>
      <div className="w-full flex items-center mt-1 justify-between">
        <h4 className="text-sm font-bold">{list.content}</h4>
        <FontAwesomeIcon
          icon={isLike ? faHeart : farHeart}
          onClick={() => onLikeContent()}
          className={`text-orange-500`}
        />
      </div>
    </li>
  );
};

export default ContentBox;
