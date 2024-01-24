'use client';

import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { handleSearchPost } from '@/app/post/actions';
import { usePathname } from 'next/navigation';
import { handleSearchCommunity } from '@/app/community/actions';

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
interface joinMemberListTypes {
  isOwner: boolean;
  memberId: number;
  nickname: string;
}
interface communityContentTypes {
  originalGoalId: number;
  originalGoalTitle: string;
  originalGoalDescription: string;
  originalGoalImageUrl: null | string;
  originalGoalshareCnt: number;
  joinMemberList: joinMemberListTypes[];
  contentList: communityContentList[];
  count: null | number;
  share: boolean;
}

const SearchBox: React.FC<{
  pageNumber: number;
  sort: string;
  onChangeSort: (sort: string) => void;
  setData:
    | React.Dispatch<React.SetStateAction<postContentTypes[]>>
    | React.Dispatch<React.SetStateAction<communityContentTypes[]>>;
  setPageable: React.Dispatch<
    React.SetStateAction<{ pageNumber: number; last: boolean }>
  >;
}> = ({ pageNumber, sort, onChangeSort, setData }) => {
  const [searchValue, setSearchValue] = useState('');

  const pathName = usePathname();

  const onSearchPost = async () => {
    const formData = {
      pageNum: pageNumber,
      query: searchValue,
      sort: sort,
    };
    const response = await handleSearchPost(formData);

    if (response.success) {
      setData(response.data.content);
      setSearchValue('');
    }
  };
  const onSearchCommunity = async () => {
    const formData = {
      pageNum: pageNumber,
      query: searchValue,
      sort: sort,
    };
    const response = await handleSearchCommunity(formData);

    if (response.success) {
      setData(response.data.content);
      setSearchValue('');
    }
  };
  return (
    <>
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11 top-7 bg-white rounded-full items-center">
        <input
          type="text"
          className="outline-0	w-4/5 pl-3 z-40"
          onChange={(e) => setSearchValue(e.target.value)}
          value={searchValue}
        ></input>
        <FontAwesomeIcon
          icon={faSearch}
          className="w-6 h-6 mr-3 text-gray-500"
          onClick={() =>
            pathName === '/post' ? onSearchPost() : onSearchCommunity()
          }
        />
      </header>
      <section className="flex gap-4 justify-end mt-2 mr-4 mb-2 h-6">
        <div className="flex items-center">
          <input
            type="radio"
            id="new"
            name="community"
            value="new"
            checked={sort === 'NEW'}
            onChange={() => onChangeSort('NEW')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="new"
          >
            최신순
          </label>
        </div>
        <div className="flex items-center">
          <input
            type="radio"
            id="popular"
            name="community"
            value="popular"
            checked={sort === 'POPULAR'}
            onChange={() => onChangeSort('POPULAR')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="popular"
          >
            인기순
          </label>
        </div>
      </section>
    </>
  );
};

export default SearchBox;
