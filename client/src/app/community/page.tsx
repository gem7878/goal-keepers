'use client';

import { CreateButton, PostBox, PostBoxDetail } from '@/components/index.js';
import React, { useEffect, useRef, useState } from 'react';
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
import { faSearch } from '@fortawesome/free-solid-svg-icons';
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

const Community = () => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [postData, setPostData] = useState<postTypes[]>([]);
  const [nickname, setNickname] = useState('');
  // const scrollRef = useRef(null);
  // const [items, setItems] = useState<any>([]);
  // const itemsPerLoad = 10;

  const dispatch = useDispatch();

  useEffect(() => {
    onGetUserInfo();
  }, []);
  const reduxPostData = useSelector(selectRender);

  useEffect(() => {
    // handleAllPost();
  }, [reduxPostData.postBoolean]);
  // useEffect(() => {
  //   // 처음 10개 렌더링
  //   if (items.length <= 0) loadItems();

  //   // 스크롤 감지
  //   const handleScroll = () => {
  //     if (scrollRef.current) {
  //       const { clientHeight, scrollHeight, scrollTop } = scrollRef.current;
  //       if (clientHeight + scrollTop >= scrollHeight) {
  //         loadItems();
  //       }
  //     }
  //   };

  //   // 이벤트 리스너 등록
  //   if (scrollRef.current) {
  //     scrollRef?.current?.addEventListener('scroll', handleScroll);
  //   }

  //   // 컴포넌트가 DOM에서 제거될 때 이벤트 리스너 삭제
  //   return () => {
  //     if (scrollRef.current) {
  //       scrollRef.current.removeEventListener('scroll', handleScroll);
  //     }
  //   };
  // }, []);
  // const loadItems = () => {
  //   const newItems = postData.slice(
  //     0,
  //     Math.min(items.length + itemsPerLoad, postData.length),
  //   );
  //   setItems(newItems);
  // };

  const handleAllPost = async ({ pageParam = 1 }) => {
    const form = { pageNum: pageParam };
    await handleGetPostAll(form)
      .then((response) => {
        setPostData(response.content);
        console.log(response);
        if (response.last) {
          return response.pageable.pageNumber + 1;
        } else {
          return response.pageable.pageNumber + 2;
        }
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
    console.log('click');

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

  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useInfiniteQuery('postQuery', handleAllPost, {
      getNextPageParam: (lastPage) => lastPage,
    });
  

  return (
    <div className="w-full	h-full pt-[80px]">
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11	fixed top-7 bg-white rounded-full items-center">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <FontAwesomeIcon
          icon={faSearch}
          className="w-6 h-6 mr-3 text-gray-500"
        />
      </header>
      <section className="z-0 h-full overflow-y-scroll w-full">
        {data?.pages.map((page, pageIndex) => (
          <React.Fragment key={pageIndex}>
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
          </React.Fragment>
        ))}
      </section>
      <CreateButton></CreateButton>
      {hasNextPage && (
        <button onClick={() => fetchNextPage()} disabled={isFetchingNextPage}>
          {isFetchingNextPage ? 'Loading...' : 'Load More'}
        </button>
      )}
    </div>
  );
};

export default Community;
