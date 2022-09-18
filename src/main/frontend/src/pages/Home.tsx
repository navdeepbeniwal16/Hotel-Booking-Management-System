import { SearchForm } from '../components/layouts/SearchForm';
import { HotelsList } from '../components/layouts/HotelsList';

export const Home = () => {
  return (
    <div>
      <SearchForm></SearchForm>
      <HotelsList></HotelsList>
    </div>
  );
};
