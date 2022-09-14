import errorGif from '@/assets/error.gif';
import errorWebp from '@/assets/error.webp';

function ErrorPlaceholder() {
  return (
    <>
      <picture>
        <source srcSet={errorWebp} type="image/webP" />
        <img src={errorGif} alt="" width="150" />
      </picture>
      <div style={{ width: '100%', textAlign: 'center' }}>오류가 발생했어요..</div>
    </>
  );
}

export default ErrorPlaceholder;
