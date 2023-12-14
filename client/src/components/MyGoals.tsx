import Image from "next/image";
import React, { SetStateAction } from "react";

interface myGoalListTypes {
  goalTitle: string;
  goalContent: string;
  image: any;
  startDate: string;
  endDate: string;
  goalComment: string[];
}

const MyGoals: React.FC<{
  myGoalList: myGoalListTypes[];
  setSelectGoalNum: React.Dispatch<SetStateAction<number | null>>;
}> = ({ myGoalList, setSelectGoalNum }) => {
  const handleSelectGoalClick = (index: number) => {
    setSelectGoalNum(index);
  };

  return (
    <div className="bg-orange-300 w-full h-[calc(100%-40px)]">
      <ul className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2">
        {myGoalList.map((list, index) => {
          return (
            <li
              key={index}
              className="w-[calc(33%-8px)] aspect-square bg-white relative flex items-center justify-center"
              onClick={() => handleSelectGoalClick(index)}
            >
              <Image
                src={list.image}
                alt=""
                style={{
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                  position: "absolute",
                }}
              ></Image>
              <div className="w-full h-full bg-black opacity-50 absolute"></div>
              <h3 className="text-white absolute">{list.goalTitle}</h3>
            </li>
          );
        })}
      </ul>
    </div>
  );
};

export default MyGoals;
