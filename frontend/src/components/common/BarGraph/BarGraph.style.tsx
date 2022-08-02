import styled from 'styled-components';

export const Container = styled.article`
  display: flex;
  position: relative;
  flex-direction: column;
  align-items: center;
  width: 448px;
  padding: 3rem 1rem;
  background-color: #ffffff;
`;

export const BarGraphTitleWrapper = styled.div`
  position: absolute;
  display: flex;
  gap: 1rem;
  margin-bottom: 0.5rem;
  bottom: 0rem;
`;

export const BarGraphTitle = styled.button`
  font-size: 1.1rem;
  background-color: #fff;
  padding: 0.2rem;
`;

export const DataWrapper = styled.div`
  display: flex;
  justify-content: center;
  gap: 2.5rem;
  height: 200px;
  width: 320px;
  border-top: 1px solid rgba(229, 233, 235, 0.7);
  border-bottom: 1px solid rgba(229, 233, 235, 0.7);
  background-image: linear-gradient(
    transparent 49px,
    rgba(229, 233, 235, 0.4) 49px
  );
  background-size: 100% 50px;
`;

export const BarWrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column-reverse;
  width: 40px;
  height: 100%;
`;

export const Bar = styled.div<{ color: string; height: number }>`
  background-color: ${({ color }) => color};
  height: ${({ height }) => `${height}%`};
  animation: graph linear 0.8s;

  @keyframes graph {
    from {
      height: 0%;
    }
    to {
    }
  }
`;

export const PercentWrapper = styled.div`
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
`;

export const Percent = styled.div`
  position: absolute;
  top: -1.3rem;
  color: ${({ theme }) => theme.colors.black};
`;

export const JobTypeWrapper = styled.div`
  display: flex;
  margin-top: 0.2rem;
  width: 320px;
`;

export const JobType = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: calc(320px / 4);
  font-size: 1.1rem;
`;
