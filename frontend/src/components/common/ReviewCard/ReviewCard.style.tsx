import styled from 'styled-components';

export const OuterContainer = styled.article`
  display: flex;
  gap: 1rem;
  border-radius: 0.375rem;
  padding: 1rem;
  width: 35rem;
  height: max-content;
  box-shadow: 0 0.35rem 0.7rem -0.2rem rgba(0, 0, 0, 0.3);
`;

export const ProductArea = styled.div`
  display: flex;
`;

export const Image = styled.img`
  width: 250px;
`;

export const Title = styled.div``;

export const ReviewArea = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const FlexColumnWrapper = styled(ReviewArea)`
  gap: 1rem;
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Content = styled.p`
  line-height: 1.4;
`;
