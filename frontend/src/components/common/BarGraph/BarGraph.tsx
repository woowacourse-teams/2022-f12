import * as S from '@/components/common/BarGraph/BarGraph.style';
import theme from '@/style/theme';
import { useState } from 'react';

type Prop = {
  statistics: Statistics;
};

function BarGraph({ statistics }: Prop) {
  const [isJobType, setIsJobType] = useState(true);

  const toggleGraph = () => {
    setIsJobType((state) => !state);
  };

  const { careerLevel, jobType } = statistics;

  return (
    <>
      {isJobType ? (
        <S.Container aria-label="통계 정보">
          <S.BarGraphTitleWrapper>
            <S.BarGraphTitle onClick={toggleGraph}>
              연차별 통계 보기
            </S.BarGraphTitle>
          </S.BarGraphTitleWrapper>
          <S.DataWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.secondary}
                height={jobType.frontend * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{jobType.frontend * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.black}
                height={jobType.backend * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{jobType.backend * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.primary}
                height={jobType.mobile * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{jobType.mobile * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.primaryDark}
                height={jobType.etc * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{jobType.etc * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
          </S.DataWrapper>
          <S.JobTypeWrapper>
            {['프론트엔드', '백엔드', '모바일', '기타'].map(
              (jobType, index) => {
                return <S.JobType key={index}>{jobType}</S.JobType>;
              }
            )}
          </S.JobTypeWrapper>
        </S.Container>
      ) : (
        <S.Container aria-label="통계 정보">
          <S.BarGraphTitleWrapper>
            <S.BarGraphTitle onClick={toggleGraph}>
              직군별 통계 보기
            </S.BarGraphTitle>
          </S.BarGraphTitleWrapper>
          <S.DataWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.secondary}
                height={careerLevel.none * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{careerLevel.none * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.black}
                height={careerLevel.junior * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{careerLevel.junior * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.primary}
                height={careerLevel.midlevel * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{careerLevel.midlevel * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
            <S.BarWrapper>
              <S.Bar
                key={Math.random()}
                color={theme.colors.primaryDark}
                height={careerLevel.senior * 100}
              />
              <S.PercentWrapper>
                <S.Percent>{careerLevel.senior * 100}%</S.Percent>
              </S.PercentWrapper>
            </S.BarWrapper>
          </S.DataWrapper>
          <S.JobTypeWrapper>
            {['경력 없음', '0-2년차', '3-5년차', '5년차 이상'].map(
              (jobType, index) => {
                return <S.JobType key={index}>{jobType}</S.JobType>;
              }
            )}
          </S.JobTypeWrapper>
        </S.Container>
      )}
    </>
  );
}

export default BarGraph;
