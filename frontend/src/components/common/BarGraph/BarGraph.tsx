import { useState } from 'react';

import * as S from '@/components/common/BarGraph/BarGraph.style';

import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';

import theme from '@/style/theme';

type Prop = {
  statistics: Statistics;
};

const jobTypeList = Object.values(JOB_TYPES);
const careerLevelList = Object.values(CAREER_LEVELS);

function BarGraph({ statistics }: Prop) {
  const [isJobType, setIsJobType] = useState(true);
  const { careerLevel, jobType } = statistics;

  const statisticsData = [
    {
      color: theme.colors.primary,
      percent: Math.round((isJobType ? jobType.frontend : careerLevel.none) * 100),
    },
    {
      color: theme.colors.primaryDark,
      percent: Math.round((isJobType ? jobType.backend : careerLevel.junior) * 100),
    },
    {
      color: theme.colors.secondary,
      percent: Math.round((isJobType ? jobType.mobile : careerLevel.midlevel) * 100),
    },
    {
      color: theme.colors.black,
      percent: Math.round((isJobType ? jobType.etc : careerLevel.senior) * 100),
    },
  ];

  const toggleGraph = () => {
    setIsJobType((state) => !state);
  };

  return (
    <S.Container aria-label="통계 정보">
      <S.DataWrapper>
        {statisticsData.map((data, index) => {
          return (
            <S.BarWrapper key={index}>
              <S.Bar key={Math.random()} color={data.color} height={data.percent} />
              <S.PercentWrapper>
                <S.Percent>{data.percent}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
          );
        })}
      </S.DataWrapper>
      <S.JobTypeWrapper>
        {isJobType
          ? jobTypeList.map((title, index) => {
              return <S.JobType key={index}>{title}</S.JobType>;
            })
          : careerLevelList.map((title, index) => {
              return <S.JobType key={index}>{title}</S.JobType>;
            })}
      </S.JobTypeWrapper>
      <S.BarGraphTitleWrapper>
        <S.BarGraphTitle>{isJobType ? '직군별 통계' : '연차별 통계'}</S.BarGraphTitle>
        <S.BarGraphToggleButton onClick={toggleGraph}>
          {isJobType ? '연차별 통계 보기' : '직군별 통계 보기'}
        </S.BarGraphToggleButton>
      </S.BarGraphTitleWrapper>
    </S.Container>
  );
}
export default BarGraph;
