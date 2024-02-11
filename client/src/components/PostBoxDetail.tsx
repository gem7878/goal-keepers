'use client';
import Image from 'next/image';
import React, { useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import { handleCreatePostContent, handleDeletePost } from '@/app/post/actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStatePost } from '@/redux/renderSlice';
import { CommentBox } from './index';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faHeart,
  faShare,
  faEdit,
  faTrash,
  faWindowClose,
  faCheckSquare,
  faCheck,
} from '@fortawesome/free-solid-svg-icons';
interface postContentContentTypes {
  content: string;
  createdAt: string;
  goalDescription: null | string;
  goalId: null | number;
  goalImageUrl: null | string;
  goalTitle: null | string;
  like: boolean;
  likeCnt: number;
  nickname: string;
}


interface postContentTypes {
  content: postContentContentTypes[];
  goalDescription: string;
  goalId: number;
  goalImageUrl: null | string;
  goalTitle: string;
  goalshareCnt: number;
  postId: number;
  share: boolean;
  isCheer: boolean;
  postCheerCnt: number;
}
interface postDataTypes {
  content: postContentTypes[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: { empty: boolean; sorted: boolean; unsorted: boolean };
    unpaged: boolean;
  };
  size: number;
  sort: { empty: boolean; sorted: boolean; unsorted: boolean };
  totalElements: number;
  totalPages: number;
}
const PostBoxDetail: React.FC<{
  data: postContentTypes;
  myNickname: string;
  index: number;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
  onLikePost: (index: number) => void;
  onShareGoal: (index: number) => void;
  onGetShareData: (index: number) => void;
}> = ({
  data,
  myNickname,
  setFocusNum,
  index,
  onLikePost,
  onShareGoal,
  onGetShareData,
}) => {
  const [createPostContent, setCreatePostContent] = useState('');
  const [postContent, setPostContent] = useState([]);
  const likeRef = useRef<HTMLUListElement>(null);
  const reduxPostData = useSelector(selectRender);
  const dispatch = useDispatch();

  const onDeletePost = async () => {
    const postData = {
      postId: data.postId,
    };
    const confirm = window.confirm('포스트를 삭제하시겠습니까?');
    if (confirm) {
      await handleDeletePost(postData)
        .then((response) => {
          setFocusNum(null);
          dispatch(setStatePost(data.postId));
        })
        .catch((error) => console.log(error));
    }
  };

  const onCreatePostContent = async (goalId: number, postId: number) => {
    console.log(goalId, postId);

    // const formData = {
    //   content: '',
    //   goalId: 1,
    // };
    // const response = await handleCreatePostContent(formData);
    // console.log(response);
  };

  return (
    <article
      className="h-[450px] flex-col p-3 mb-4 border rounded-md duration-100	
      w-11/12
      inset-x-0
      mx-auto"
    >
      <div className="w-full h-1/4 relative z-0 flex rounded-md">
        <Image
          src={data.goalImageUrl === null ? Image1 : data.goalImageUrl}
          // src={Image1}
          alt=""
          fill
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
        {/* {myNickname === data.content.nickname && (
          <div className="flex text-white absolute top-0 right-0 text-xs gap-2 m-2">
            <>
              <FontAwesomeIcon onClick={() => onDeletePost()} icon={faTrash} />
            </>
          </div>
        )} */}
        <h3 className="text-center px-1  mx-4	text-white	font-bold absolute top-1/4 -translate-y-1/3 z-10 text-ellipsis	">
          {data.goalTitle.length > 18
            ? data.goalTitle.slice(0, 18) + '...'
            : data.goalTitle}
        </h3>
        <p className="text-white w-5/6 absolute top-1/3 text-xs mt-2 mx-4">
          {data.goalDescription.length > 65
            ? data.goalDescription.slice(0, 65) + '...'
            : data.goalDescription}
        </p>
        <ul
          ref={likeRef}
          className="absolute right-0 bottom-0 mb-1 mr-3 flex justify-center	text-white gap-2"
        >
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faHeart}
              onClick={() => onLikePost(index)}
              className="text-orange-500"
            />
            {/* <label
              className={`text-xs	${
                data.isCheer ? 'text-orange-400' : 'text-gray-300'
              }`}
            >
              {data.postCheerCnt}
            </label> */}
          </li>
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faShare}
              onClick={() => {
                data.share ? onGetShareData(data.goalId) : onShareGoal(index);
              }}
              className="text-gray-400"
            />
            <label
              className={`text-xs	${
                data.share ? 'text-orange-400' : 'text-gray-300'
              }`}
            >
              {data.goalshareCnt}
            </label>
          </li>
        </ul>
      </div>

      <div className="w-full h-[45%]	mt-2 flex flex-col">
        <ul className="flex-1 overflow-y-auto"></ul>
        {/* {myNickname === data.isMyPost && (
          <button
            onClick={() => onCreatePostContent(data.goalId, data.postId)}
            className="h-[13%] w-full bg-orange-400 rounded-xl text-sm text-white"
          >
            기록하기
          </button>
        )} */}
      </div>
      <CommentBox postId={data.postId} myNickname={myNickname}></CommentBox>
    </article>
  );
};
export default PostBoxDetail;
