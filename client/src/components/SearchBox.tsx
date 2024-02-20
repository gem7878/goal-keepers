import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faSearch,
} from '@fortawesome/free-solid-svg-icons';

const SearchBox: React.FC<{
  sort: string;
  onChangeSort: (sort: string) => void;
}> = ({ sort, onChangeSort }) => {
  return (
    <>
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11 top-7 bg-white rounded-full items-center">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <FontAwesomeIcon
          icon={faSearch}
          className="w-6 h-6 mr-3 text-gray-500"
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
