import React from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import Image from 'next/image';

interface communityContentList {
  nickname: string;
  content: number;
  updatedAt: string;
  likeCnt: number;
  goalId: number;
  goalTitle: string;
  goalDescription: string;
  goalImageUrl: null | string;
  like: false;
}
interface communityContentTypes {
  originalGoalId: number;
  originalGoalTitle: string;
  originalGoalDescription: string;
  originalGoalImageUrl: null | string;
  originalGoalshareCnt: number;
  joinMemberList: string[];
  contentList: communityContentList[];
  count: null | number;
  share: boolean;
}
const CommunityBoxDetail: React.FC<{ data: communityContentTypes; }> = ({
  data,
}) => {
  return (
    <article
      className="h-4/5
      flex
      justify-between
      p-3
      mb-4
      border
      rounded-md
      duration-100
      w-10/12
      inset-x-0
      mx-auto
       "
    >
      <div className="w-full relative">
        <Image
          src={
            data.originalGoalImageUrl === null
              ? Image1
              : data.originalGoalImageUrl
          }
          alt=""
          fill
          style={{
            objectFit: 'cover',
            zIndex: 1,
          }}
        ></Image>
        <div className="bg-black opacity-40 z-10 w-full h-full absolute"></div>
        <div className="absolute z-20 w-full h-full p-3 flex flex-col">
          <section className="h-1/6">
            <h3 className="text-white text-xl">{data.originalGoalTitle}</h3>
          </section>
          <section className="h-4/6">
            <ul className="w-full">
              {data.contentList.map((list, index) => {
                return (
                  <li
                    key={index}
                    className={`text-gray-600 bg-orange-100 mt-3 py-1 rounded px-2 drop-shadow-lg`}
                  >
                    <div>
                      <h4 className="text-sm font-bold">{list.content}</h4>
                    </div>

                    <div>
                      <div className="rounded-full h-5 bg-gray-600 px-2 inline-block">
                        <p className="text-orange-300 text-[11px] leading-5 inline-block">
                          {list.nickname}
                        </p>
                      </div>
                      <span>{list.updatedAt}</span>
                    </div>
                  </li>
                );
              })}
            </ul>
          </section>
          <section className="h-1/6">
            <ul>
              {data.joinMemberList.map((list, index) => {
                return <li key={index}>{list}</li>;
              })}
            </ul>
          </section>
        </div>
      </div>
    </article>
  );
};

export default CommunityBoxDetail;
