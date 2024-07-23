package com.praveen.sqs.mapper;

import com.praveen.sqs.entity.AddressEntity;
import com.praveen.sqs.entity.CompanyEntity;
import com.praveen.sqs.entity.GeoLocation;
import com.praveen.sqs.entity.UserEntity;
import com.praveen.sqs.model.User;

public interface UserMapper {

  static UserEntity toEntity(User user, String uuid) {

    final UserEntity userEntity = new UserEntity();

    userEntity.setName(user.name());
    userEntity.setUserName(user.userName());
    userEntity.setPhone(user.phone());
    userEntity.setEmail(user.email());
    userEntity.setWebsite(user.website());
    userEntity.setCorrelationId(uuid);

    final AddressEntity addressEntity = new AddressEntity();
    addressEntity.setCity(user.address().city());
    addressEntity.setSuite(user.address().suite());
    addressEntity.setStreet(user.address().street());
    addressEntity.setZipcode(user.address().zipcode());

    final GeoLocation geoLocation =
        new GeoLocation(user.address().geo().latitude(), user.address().geo().longitude());

    addressEntity.setGeoLocation(geoLocation);

    userEntity.setAddress(addressEntity);

    CompanyEntity companyEntity = new CompanyEntity();
    companyEntity.setCatchPhrase(user.company().catchPhrase());
    companyEntity.setTagLine(user.company().bs());
    companyEntity.setName(user.company().name());

    userEntity.setCompany(companyEntity);

    return userEntity;
  }
}
