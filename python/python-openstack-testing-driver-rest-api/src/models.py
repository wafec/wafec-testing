from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, create_engine
from sqlalchemy.orm import sessionmaker, relationship
import datetime

Base = declarative_base()


class Key(Base):
    __tablename__ = 'KEY'

    id = Column(Integer, primary_key=True)
    username = Column(String)
    password = Column('passwd', String)
    project_name = Column(String)
    user_domain_name = Column(String)
    project_domain_name = Column(String)
    auth_url = Column(String)


engine = create_engine('mysql+pymysql://testing:testing-321@localhost/testing', pool_recycle=3600)
Session = sessionmaker(bind=engine)
