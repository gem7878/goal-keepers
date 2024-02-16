import React from 'react';
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
const CommunityBoxDetail: React.FC<{ data: communityContentTypes }> = ({
  data,
}) => {
  return (
    <div>
      <h2>커뮤니티박스 디테일</h2>
    </div>
  );
};

export default CommunityBoxDetail;
