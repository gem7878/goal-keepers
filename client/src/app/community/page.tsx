'use client';
import { CreateButton, PostBox, PostBoxDetail } from '@/components/index.js';
import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import { handleGetPostAll, handleLikePost } from './actions';
import { handleGetUserInfo } from '../actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStatePost } from '@/redux/renderSlice';
import {
  handleCreateShare,
  handleDeleteShare,
  handleGetShare,
} from './share/actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faSearch,
  faChevronCircleRight,
  faChevronCircleLeft,
} from '@fortawesome/free-solid-svg-icons';
import { useQuery, useInfiniteQuery } from 'react-query';

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
const Community = (props: any) => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [postData, setPostData] = useState<postTypes[]>([]);
  const [nickname, setNickname] = useState('');
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [page, setPage] = useState(pageable.pageNumber);

  const dispatch = useDispatch();
  const reduxPostData = useSelector(selectRender);

  useEffect(() => {
    onGetUserInfo();
  }, []);
  useEffect(() => {
    onGetAllPost(page);
  }, [page, reduxPostData.postBoolean]);

  const onGetAllPost = async (pageParam: number) => {
    const form = { pageNum: pageParam };
    await handleGetPostAll(form)
      .then((response) => {
        setPostData(response.content);
        setPageable({
          pageNumber: response.pageable.pageNumber + 1,
          last: response.last,
        });
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
  const onShareGoal = async (index: number) => {
    await handleCreateShare(postData[index].goalId)
      .then((response) => {
        if (response.success) {
          dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };
  const onGetShareData = async (goalId: number) => {
    await handleGetShare(goalId)
      .then(async (response) => {
        if (response.success) {
          await onDeleteShareGoal(response.data.goalId);
        }
      })
      .catch((error) => console.log(error));
  };
  const onDeleteShareGoal = async (goalId: number) => {
    await handleDeleteShare(goalId)
      .then((response) => {
        if (response.success) {
          dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };

  return (
    <div className="w-full	h-full pt-[80px]">
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11	fixed top-7 bg-white rounded-full items-center">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <FontAwesomeIcon
          icon={faSearch}
          className="w-6 h-6 mr-3 text-gray-500"
        />
      </header>
      <section className="z-0 h-full overflow-y-scroll w-full postList">
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
                onShareGoal={onShareGoal}
                onGetShareData={onGetShareData}
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
                onShareGoal={onShareGoal}
                onGetShareData={onGetShareData}
              ></PostBox>
            );
          }
        })}

        <section className="h-[30px] w-full flex justify-center gap-4 text-gray-600 ">
          <FontAwesomeIcon
            icon={faChevronCircleLeft}
            className="cursor-pointer"
            onClick={() => {
              if (page > 1) {
                setPage(page - 1);
                setFocusNum(null);
              } else {
                alert('첫 번째 페이지 입니다.');
              }
            }}
          />
          <FontAwesomeIcon
            icon={faChevronCircleRight}
            className="cursor-pointer"
            onClick={() => {
              if (pageable.last) {
                alert('마지막 페이지 입니다.');
              } else {
                setPage(page + 1);
                setFocusNum(null);
              }
            }}
          />
        </section>
      </section>
      <CreateButton></CreateButton>
    </div>
  );
};
export default Community;
