'use client';

import Image from 'next/image';
import React, {
  SetStateAction,
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import Image2 from '@/public/assets/images/gem.png';
import { useDispatch, useSelector } from 'react-redux';
import {
  selectRender,
  setStateAlarmTarget,
  setStatePost,
} from '@/redux/renderSlice';
import { handleGetMyPostAll, handleGetPostAll } from '@/app/post/actions';
import { setStateGoal } from '@/redux/renderSlice';
import { PostBox, PostBoxDetail } from './index';
import { handleGetUserInfo } from '@/app/actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faChevronCircleRight,
  faChevronCircleLeft,
} from '@fortawesome/free-solid-svg-icons';
import {
  handleCreateShare,
  handleDeleteShare,
  handleGetShare,
} from '@/app/community/share/actions';
import { selectAlarmData } from '@/redux/alarmDataSlice';

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

const MyPosts: React.FC<{}> = ({}) => {
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [nickname, setNickname] = useState('');
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [page, setPage] = useState(pageable.pageNumber);
  const [postData, setPostData] = useState<postTypes[]>([]);

  const containerRef = useRef<any>(null);

  const dispatch = useDispatch();
  const reduxPostData = useSelector(selectRender);
  const reduxAlarmData = useSelector(selectAlarmData);

  useEffect(() => {
    onGetUserInfo();
  }, []);

  useEffect(() => {
    onGetMyAllPost(page);
  }, [page, reduxPostData.postBoolean, reduxPostData.privateBoolean]);

  useEffect(() => {
    if (reduxPostData.alarmBoolean) {
      onGetMyTargetPost(reduxAlarmData.targetPage, reduxAlarmData.targetId);
    }
  }, [reduxPostData.alarmBoolean]);

  const onGetMyAllPost = async (pageParam: number) => {
    const form = { pageNum: pageParam };
    const response = await handleGetMyPostAll(form);

    if (response.success) {
      setPostData(response.data.content);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });
    }
  };

  const onGetMyTargetPost = async (pageParam: number, targetId: number) => {
    const form = { pageNum: pageParam };
    const response = await handleGetMyPostAll(form);

    if (response.success) {
      const responseData = response.data.content;
      const index = responseData.findIndex(
        (item: { postId: number }) => item.postId === targetId,
      );

      setPostData(responseData);
      setFocusNum(index);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });

      return containerRef.current.scrollTo({ top: 176 * index });
    }
  };

  const onGetUserInfo = async () => {
    await handleGetUserInfo()
      .then((response) => {
        setNickname(response.nickname);
      })
      .catch((error) => console.log(error));
  };

  const onCheerPost = async (index: number) => {
    // await handleLikePost(postData[index].postId)
    //   .then((response) => {
    //     if (response.success) {
    //       dispatch(setStatePost(!reduxPostData.postBoolean));
    //     }
    //   })
    //   .catch((error) => console.log(error));
  };
  const onShareGoal = async (index: number) => {
    // await handleCreateShare(postData[index].goalId)
    //   .then((response) => {
    //     if (response.success) {
    //       dispatch(setStatePost(!reduxPostData.postBoolean));
    //     }
    //   })
    //   .catch((error) => console.log(error));
  };
  const onGetShareData = async (goalId: number) => {
    // await handleGetShare(goalId)
    //   .then(async (response) => {
    //     if (response.success) {
    //       await onDeleteShareGoal(response.data.goalId);
    //     }
    //   })
    //   .catch((error) => console.log(error));
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
    <div className="w-full h-[calc(100%-40px)] border-x border-b border-orange-300">
      <ul
        className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2"
        ref={containerRef}
      >
        <section className="z-0 h-full w-full postList">
          {postData.map((data, index) => {
            if (focusNum === index) {
              return (
                <PostBoxDetail
                  data={data}
                  key={index}
                  myNickname={nickname}
                  setFocusNum={setFocusNum}
                  index={index}
                  onCheerPost={onCheerPost}
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
                  onCheerPost={onCheerPost}
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
      </ul>
    </div>
  );
};
export default MyPosts;
