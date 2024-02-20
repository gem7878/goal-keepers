'use client';
import Image from 'next/image';
import React, { useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import {
  handleCreatePostContent,
  handleDeletePost,
  handleDeletePostContent,
  handleGetAllPostContent,
  handleLikeContent,
} from '@/app/post/actions';
import { useDispatch, useSelector } from 'react-redux';
import {
  selectRender,
  setStateContent,
  setStatePost,
} from '@/redux/renderSlice';
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
  faTimes,
  faThumbsUp,
  faTrashAlt,
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
  contentId: number;
}

interface postContentTypes {
  content: postContentContentTypes;
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
}
const PostBoxDetail: React.FC<{
  data: postContentTypes;
  myNickname: string;
  index: number;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
  onCheerPost: (index: number) => void;
  onShareGoal: (index: number) => void;
  onGetShareData: (index: number) => void;
}> = ({
  data,
  myNickname,
  setFocusNum,
  index,
  onCheerPost,
  onShareGoal,
  onGetShareData,
}) => {
  const [addContent, setAddContent] = useState(false);
  const [contentList, setContentList] = useState<postContentContentTypes[]>([]);
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const [focusContent, setFocusContent] = useState<null | number>(null);
  const [contentValue, setContentValue] = useState('');

  const likeRef = useRef<HTMLUListElement>(null);
  const contentRef = useRef<any>(null);
  const reduxPostData = useSelector(selectRender);
  const dispatch = useDispatch();

  useEffect(() => {
    onGetAllPostContent(pageable.pageNumber);
  }, [reduxPostData.contentBoolean]);

  const onGetAllPostContent = async (pageNumber: number) => {
    const formData = {
      pageNum: pageNumber,
      postId: data.postId,
    };

    const response = await handleGetAllPostContent(formData);

    if (response.success) {
      if (more) {
        setContentList((prevPostData) => [
          ...prevPostData,
          ...response.data.content,
        ]);
        setPageable({
          pageNumber: response.data.pageable.pageNumber + 1,
          last: response.data.last,
        });
      } else {
        setContentList(response.data.content);
        setPageable({
          pageNumber: response.data.pageable.pageNumber + 1,
          last: response.data.last,
        });
      }
      setMore(false);
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
          dispatch(setStatePost(data.postId));
        })
        .catch((error) => console.log(error));
    }
  };

  const onCreatePostContent = async (goalId: number) => {
    const formData = {
      content: contentValue,
      goalId: goalId,
    };
    const response = await handleCreatePostContent(formData);

    if (response.success) {
      setAddContent(false);
      dispatch(setStateContent(!reduxPostData.contentBoolean));
    }
  };

  const onLikeContent = async (contentId: number) => {
    const response = await handleLikeContent(contentId);

    if (response.success) {
      dispatch(setStateContent(!reduxPostData.contentBoolean));
    }
  };
  const onDeleteContent = async (contentId: number) => {
    const response = await handleDeletePostContent(contentId);

    if (response.success) {
      dispatch(setStateContent(!reduxPostData.contentBoolean));
    }
  };

  return (
    <article
      className="h-[450px] flex flex-col p-3 mb-4 border rounded-md duration-100	
      w-11/12
      inset-x-0
      mx-auto justify-between
      "
    >
      <div className="w-full h-1/4 relative z-0 flex rounded-md">
        <Image
          src={data.goalImageUrl === null ? Image1 : data.goalImageUrl}
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
        {data.myPost && (
          <div className="flex text-white absolute top-0 right-0 text-xs gap-2 m-2">
            <>
              <FontAwesomeIcon
                onClick={() => onDeletePost()}
                icon={faTrashAlt}
              />
            </>
          </div>
        )}
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
              icon={faThumbsUp}
              onClick={() => onCheerPost(index)}
              className={`text-gray-400 ${
                data.cheer ? 'text-orange-400' : 'text-gray-300'
              }`}
            />
            <label className={`text-xs 'text-gray-300'`}>
              {data.postCheerCnt}
            </label>
          </li>
          <li className="flex items-center gap-1">
            <FontAwesomeIcon
              icon={faShare}
              onClick={() => {
                data.share ? onGetShareData(data.goalId) : onShareGoal(index);
              }}
              className={`text-gray-400 ${
                data.share ? 'text-orange-400' : 'text-gray-300'
              }`}
            />
            <label className={`text-xs 'text-gray-300'	`}>
              {data.goalshareCnt}
            </label>
          </li>
        </ul>
      </div>

      <div className="w-full	mt-2 flex flex-col flex-1">
        <ul className="flex-1 overflow-y-auto w-full p-2 pb-4" ref={contentRef}>
          {addContent && (
            <li className="w-full h-9 flex gap-2 items-center">
              <input
                className="w-11/12 text-sm border-b p-1 text-gray-600"
                type="text"
                autoFocus
                placeholder="목표의 현재 진행 상황을 기록하세요!"
                onChange={(e) => setContentValue(e.target.value)}
              ></input>
              <button className="w-6 h-6" onClick={() => setAddContent(false)}>
                <FontAwesomeIcon
                  icon={faTimes}
                  className="w-full text-gray-600 h-full"
                />
              </button>
            </li>
          )}
          {contentList.map((list, index) => {
            return (
              <li
                key={index}
                onMouseEnter={() => setFocusContent(index)}
                onMouseLeave={() => setFocusContent(null)}
                className={`text-gray-600 font-semibold	 text-sm ${
                  focusContent === index ? 'bg-orange-200' : 'bg-orange-100'
                } mt-3 py-1 rounded-md px-2 drop-shadow-md flex justify-between`}
              >
                <span>{list.content}</span>
                <button>
                  {focusContent === index ? (
                    data.myPost ? (
                      <FontAwesomeIcon
                        onClick={() => onDeleteContent(list.contentId)}
                        className={`text-white`}
                        icon={faTrashAlt}
                      />
                    ) : (
                      <FontAwesomeIcon
                        icon={faHeart}
                        className={` ${
                          list.like ? 'text-orange-600' : 'text-white'
                        }`}
                        onClick={() => onLikeContent(list.contentId)}
                      />
                    )
                  ) : (
                    <></>
                  )}
                </button>
              </li>
            );
          })}
        </ul>
        {data.myPost && (
          <button
            onClick={() => {
              if (addContent) {
                onCreatePostContent(data.goalId);
              } else {
                contentRef.current.scrollTop = 0;
                setAddContent(true);
              }
            }}
            className="h-[13%] w-full bg-orange-400 rounded-xl text-sm text-white"
          >
            {addContent ? '입력' : '기록하기'}
          </button>
        )}
      </div>
      <CommentBox postId={data.postId} myNickname={myNickname}></CommentBox>
    </article>
  );
};
export default PostBoxDetail;
