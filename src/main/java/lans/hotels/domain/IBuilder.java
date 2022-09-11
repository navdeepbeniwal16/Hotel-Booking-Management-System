package lans.hotels.domain;

public interface IBuilder<ResultType> {
    void reset();
    ResultType getResult();
}
