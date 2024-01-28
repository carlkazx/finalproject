import { IoTimer } from "react-icons/io5";
import { FaTachometerAlt, FaTasks, FaStickyNote, FaRegChartBar, FaRegCalendarAlt, FaChevronRight, FaChevronLeft, FaBolt } from "react-icons/fa";
import { Link } from 'react-router-dom';
const Sidebar = () => {
    return (
        <div className="">
            <div className='bg-[#4E73DF] px-[25px] h-screen'>
                <div
                    className='px-[15px] py-[30px] flex items-center justify-center border-b-[1px] border-[#EDEDED]/[0.3]'>
                    <h1 className='text-white text-[20px] leading-[24px] font-extrabold cursor-pointer'>Admin Panel</h1>
                </div>
                <div
                    className='flex items-center gap-[15px] py-[20px] border-b-[1px] border-[#EDEDED]/[0.3] cursor-pointer'>
                    <FaTachometerAlt color='white'/>
                    <p className='text-[14px] leading-[20px] font-bold text-white'>Dashboard</p>
                </div>

                <div className='pt-[15px] border-b-[1px] border-[#EDEDED]/[0.3]'>
                    <p className='text-[10px] font-extrabold leading-[16px] text-white/[0.4]'> INTERFACE</p>
                    {/* Tasks Link */}
                    <Link to="/tasks" className='text-decoration-none'> {/* Add this line */}
                        <div className='flex items-center justify-between gap-[10px] py-[15px] cursor-pointer'>
                            <div className='flex items-center gap-[10px]'>
                                <FaTasks color='white'/>
                                <p className='text-[14px] leading-[20px] font-normal text-white'>Tasks</p>
                            </div>
                            <FaChevronRight color='white'/>
                        </div>
                    </Link> {/* Close Link tag here */}
                    <div className='flex items-center justify-between gap-[10px] py-[15px] cursor-pointer'>
                        <div className='flex items-center gap-[10px]'>
                            <IoTimer color='white'/> <p
                            className='text-[14px] leading-[20px] font-normal text-white'>Overtime</p>
                        </div>
                        <FaChevronRight color='white'/>
                    </div>
                </div>
                <div className='pt-[15px] border-b-[1px] border-[#EDEDED]/[0.3]'>
                    <p className='text-[10px] font-extrabold leading-[16px] text-white/[0.4]'> ADDONS</p>
                    <div className='flex items-center justify-between gap-[10px] py-[15px] cursor-pointer'>
                        <div className='flex items-center gap-[10px]'>
                            <FaStickyNote color='white'/> <p
                            className='text-[14px] leading-[20px] font-normal text-white'>Pages</p>
                        </div>
                        <FaChevronRight color='white'/>
                    </div>
                    <div className='flex items-center gap-[10px] py-[15px]  cursor-pointer'>
                        <FaRegChartBar color='white'/> <p
                        className='text-[14px] leading-[20px] font-normal text-white'>Charts</p>
                    </div>
                    <div className='flex items-center gap-[10px] py-[15px] cursor-pointer'>
                        <FaRegCalendarAlt color='white'/> <p
                        className='text-[14px] leading-[20px] font-normal text-white'>Tables</p>
                    </div>
                </div>
                <div className='pt-[15px]'>
                    <div className='flex items-center justify-center'>

                        <div
                            className='h-[40px] w-[40px] bg-[#3C5EC1] rounded-full flex items-center justify-center cursor-pointer'>
                            <FaChevronLeft color='white'/>
                        </div>
                    </div>
                </div>
                <div
                    className='bg-[#395CBF] mt-[15px] flex items-center justify-center flex-col py-[15px] px-[10px] gap-[15px] rounded-[3px]'>
                    <FaBolt color='white'/>
                    <p className='text-[12px] leading-[18px] font-normal text-white/[0.4] text-center'>Lorem
                        ipsum dolor
                        sit amet consectetur adipisicing elit. Quam, soluta.</p>
                    <button
                        className='bg-[#17A673] text-white flex items-center justify-center h-[30px] w-full rounded-[3px] text-[14px] leading-[21px] font-normal'>Upgrade
                        to Pro!
                    </button>
                </div>
            </div>
        </div>

    )
}

export default Sidebar