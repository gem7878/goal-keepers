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
import { selectRender, setStatePost } from '@/redux/renderSlice';
import {
  handleGetMyPostAll,
  handleGetPostAll,
  handleLikePost,
} from '@/app/post/actions';
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
  myPost: boolean;
}

interface myPostListTypes {
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
const MyPosts: React.FC<{
  myPostList: myPostListTypes[];
  setMyPostList: any;
}> = ({ myPostList, setMyPostList }) => {
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const [nickname, setNickname] = useState('');
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [page, setPage] = useState(pageable.pageNumber);
  const [postData, setPostData] = useState<postContentTypes[]>([]);

  const containerRef = useRef<any>(null);

  const dispatch = useDispatch();
  const reduxPostData = useSelector(selectRender);

  useEffect(() => {
    onGetUserInfo();
  }, []);

  useEffect(() => {
    onGetMyAllPost(page);
  }, [page, reduxPostData.postBoolean]);

  const onGetMyAllPost = async (pageParam: number) => {
    const form = { pageNum: pageParam };
    await handleGetMyPostAll(form)
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
      </ul>
    </div>
  );
};
export default MyPosts;
