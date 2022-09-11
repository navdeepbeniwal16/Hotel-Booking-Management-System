package lans.hotels.domain;

public interface IReferenceObject<UIDType> {
    UIDType getUid();
    boolean equals(Object otherReferenceObject);
}
